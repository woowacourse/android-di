package woowacourse.di

import java.lang.annotation.RetentionPolicy
import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.full.cast
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

class Injector private constructor(
    private val registry: Registry,
    private var parent: Injector?,
    val id: String,
    val scopeType: ScopeType?,
) : AutoCloseable {
    constructor() : this(Registry(), null, "root", null)

    private val isRoot = parent == null
    private val children = mutableMapOf<String, Injector>()
    private val instances = mutableMapOf<DependencyKey, Instance>()
    private var closed = false

    val isClosed: Boolean get() = synchronized(registry) { closed }
    internal val instanceCount: Int get() = synchronized(registry) { instances.size }
    internal val childCount: Int get() = synchronized(registry) { children.size }

    fun openScope(
        id: String,
        type: ScopeType,
    ): Injector =
        synchronized(registry) {
            checkOpen()
            require(id.isNotBlank()) { "스코프 ID는 비어 있을 수 없습니다." }
            children[id]?.let {
                require(it.scopeType == type) { "다른 타입으로 열린 스코프입니다: $id" }
                return@synchronized it
            }
            Injector(registry, this, id, type).also { children[id] = it }
        }

    fun <T : Any> registerSingleton(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        onClose: (T) -> Unit = {},
        provider: Injector.() -> T,
    ) {
        register(type, qualifier, null, provider, onClose)
    }

    fun <T : Any> registerScoped(
        type: KClass<T>,
        scope: ScopeType,
        qualifier: KClass<out Annotation>? = null,
        onClose: (T) -> Unit = {},
        provider: (Injector.() -> T)? = null,
    ) {
        register(type, qualifier, scope, provider, onClose)
    }

    private fun <T : Any> register(
        type: KClass<T>,
        qualifier: KClass<out Annotation>?,
        scope: ScopeType?,
        provider: (Injector.() -> T)?,
        onClose: (T) -> Unit,
    ) = synchronized(registry) {
        checkOpen()
        check(isRoot) { "의존성은 루트 컨테이너에서 등록해야 합니다." }
        qualifier?.let(::validateQualifier)
        val key = DependencyKey(type, qualifier)
        require(key !in registry.definitions && key !in instances) {
            "이미 등록되거나 생성된 의존성입니다: $key"
        }
        registry.definitions[key] = Definition(scope, provider) { onClose(type.cast(it)) }
    }

    fun <T : Any> create(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T =
        synchronized(registry) {
            checkOpen()
            val key = resolveKey(type, qualifier)
            val definition = registry.definitions[key]
            val owner = if (definition == null) this else ownerOf(definition.scope)
            owner.resolve(type, key, definition)
        }

    private fun ownerOf(scope: ScopeType?): Injector {
        val ancestors = generateSequence(this) { it.parent }
        return if (scope == null) {
            ancestors.last()
        } else {
            requireNotNull(ancestors.firstOrNull { it.scopeType == scope }) {
                "활성 스코프가 필요합니다: ${scope.name} (요청 스코프: $id)"
            }
        }
    }

    private fun <T : Any> resolve(
        type: KClass<T>,
        key: DependencyKey,
        definition: Definition?,
    ): T {
        checkOpen()
        instances[key]?.let { return type.cast(it.value) }
        val resolution = this to key
        check(resolution !in registry.resolving) {
            val path = (registry.resolving + resolution).joinToString(" -> ") { it.second.toString() }
            "순환 의존성이 발견되었습니다: $path"
        }
        registry.resolving.add(resolution)
        try {
            val instance =
                definition?.provider?.let { type.cast(it(this)) }
                    ?: type.objectInstance
                    ?: construct(type)
            injectFields(instance)
            checkOpen()
            if (definition != null || type.objectInstance != null) {
                instances[key] = Instance(instance, definition?.onClose ?: {})
            }
            return instance
        } finally {
            registry.resolving.removeAt(registry.resolving.lastIndex)
        }
    }

    override fun close(): Unit =
        synchronized(registry) {
            if (closed) return@synchronized
            closed = true
            parent?.children?.remove(id)
            parent = null
            val childScopes = children.values.toList()
            val ownedInstances = instances.values.toList().asReversed()
            children.clear()
            instances.clear()
            if (isRoot) registry.definitions.clear()
            var failure: Throwable? = null
            for (release in childScopes.map { { it.close() } } + ownedInstances.map { { it.onClose(it.value) } }) {
                try {
                    release()
                } catch (error: Throwable) {
                    if (failure == null) failure = error else failure.addSuppressed(error)
                }
            }
            failure?.let { throw it }
        }

    private fun checkOpen() {
        check(!closed) { "이미 닫힌 스코프입니다: $id" }
    }

    private class Registry {
        val definitions = mutableMapOf<DependencyKey, Definition>()
        val resolving = mutableListOf<Pair<Injector, DependencyKey>>()
    }

    private class Definition(
        val scope: ScopeType?,
        val provider: (Injector.() -> Any)?,
        val onClose: (Any) -> Unit,
    )

    private class Instance(
        val value: Any,
        val onClose: (Any) -> Unit,
    )

    private fun resolveKey(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
    ): DependencyKey {
        qualifier?.let(::validateQualifier)
        val key = DependencyKey(type, qualifier)
        val candidates =
            registry.definitions.keys
                .filter { it.type == type }
                .sortedBy { it.toString() }
        require(qualifier != null || candidates.size <= 1) {
            "여러 의존성이 등록되어 Qualifier가 필요합니다: ${type.simpleName}. 등록된 후보: $candidates"
        }
        if (qualifier != null) {
            require(key in registry.definitions) {
                "등록되지 않은 의존성입니다: $key. 등록된 후보: $candidates"
            }
        } else {
            require(candidates.isEmpty() || key in registry.definitions) {
                "등록된 의존성의 Qualifier를 지정해야 합니다: ${type.simpleName}. 등록된 후보: $candidates"
            }
        }
        return key
    }

    private fun validateQualifier(qualifier: KClass<out Annotation>) {
        require(qualifier.findAnnotation<Qualifier>() != null) {
            "@Qualifier가 붙은 애노테이션만 사용할 수 있습니다: ${qualifier.simpleName}"
        }
        val retention = qualifier.java.getAnnotation(java.lang.annotation.Retention::class.java)
        require(retention?.value == RetentionPolicy.RUNTIME) {
            "Qualifier는 RUNTIME 보존 정책이 필요합니다: ${qualifier.simpleName}"
        }
        require(qualifier.java.declaredMethods.isEmpty()) {
            "Qualifier는 값 없이 애노테이션 타입으로 구분해야 합니다: ${qualifier.simpleName}"
        }
    }

    private fun qualifierOn(
        annotations: List<Annotation>,
        target: String,
    ): KClass<out Annotation>? {
        val qualifiers =
            annotations.map { it.annotationClass }.filter { it.findAnnotation<Qualifier>() != null }
        require(qualifiers.size <= 1) {
            "주입 대상에는 Qualifier를 하나만 지정해야 합니다: $target. 발견된 Qualifier: $qualifiers"
        }
        return qualifiers.singleOrNull()
    }

    private fun <T : Any> construct(type: KClass<T>): T {
        require(!type.isAbstract) {
            "인터페이스나 추상 클래스의 생성 방법을 등록해야 합니다: ${type.simpleName}"
        }
        val constructor =
            requireNotNull(type.primaryConstructor) {
                "${type.simpleName}의 주 생성자를 찾을 수 없습니다."
            }
        val arguments =
            constructor.parameters.map { parameter ->
                val dependencyType =
                    parameter.type.classifier as? KClass<*>
                        ?: error("지원하지 않는 타입: ${parameter.type}")
                val qualifier = qualifierOn(parameter.annotations, "${type.simpleName}.${parameter.name}")
                create(dependencyType, qualifier)
            }
        return constructor.call(*arguments.toTypedArray())
    }

    private fun injectFields(instance: Any) {
        generateSequence<Class<*>>(instance.javaClass) { it.superclass }
            .flatMap { it.declaredFields.asSequence() }
            .filter { it.isAnnotationPresent(Inject::class.java) }
            .forEach { field ->
                require(!Modifier.isFinal(field.modifiers) && !Modifier.isStatic(field.modifiers)) {
                    "@Inject는 변경 가능한 인스턴스 필드에만 사용할 수 있습니다: ${field.name}"
                }
                val qualifier = qualifierOn(field.annotations.toList(), "${field.declaringClass.simpleName}.${field.name}")
                field.isAccessible = true
                field.set(instance, create(field.type.kotlin, qualifier))
            }
    }
}
