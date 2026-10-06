package woowacourse.di

import kotlin.reflect.KClass
import kotlin.reflect.full.cast
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

data class ScopeType(
    val name: String,
)

class DependencyContainer(
    private val bindings: Map<DependencyKey, KClass<*>> = emptyMap(),
    private val scopes: Map<DependencyKey, ScopeType> = emptyMap(),
) {
    fun openScope(type: ScopeType): DependencyScope = DependencyScope(this, type)

    internal fun <T : Any> get(
        type: KClass<T>,
        qualifier: KClass<out Annotation>?,
        scope: DependencyScope,
    ): T = type.cast(resolve(DependencyKey(type, qualifier), scope, mutableListOf()))

    internal fun create(
        type: KClass<*>,
        scope: DependencyScope,
    ): Any = create(type, scope, mutableListOf())

    internal fun inject(
        target: Any,
        scope: DependencyScope,
    ) {
        scope.checkOpen()
        target.javaClass.declaredFields
            .filter { it.isAnnotationPresent(Inject::class.java) }
            .forEach { field ->
                field.isAccessible = true
                field.set(
                    target,
                    resolve(
                        DependencyKey(field.type.kotlin, field.annotations.toList().findQualifier()),
                        scope,
                        mutableListOf(),
                    ),
                )
            }
    }

    private fun create(
        type: KClass<*>,
        scope: DependencyScope,
        creating: MutableList<KClass<*>>,
    ): Any {
        scope.checkOpen()
        check(type !in creating) {
            "순환 의존성: ${(creating + type).joinToString(" -> ") { it.simpleName ?: it.toString() }}"
        }
        creating += type
        return try {
            val constructor = type.primaryConstructor ?: error("주 생성자를 찾을 수 없습니다: ${type.qualifiedName}")
            constructor.isAccessible = true
            val arguments =
                constructor.parameters.associateWith { parameter ->
                    resolve(
                        DependencyKey(parameter.type.jvmErasure, parameter.annotations.findQualifier()),
                        scope,
                        creating,
                    )
                }
            constructor.callBy(arguments)
        } finally {
            creating.removeAt(creating.lastIndex)
        }
    }

    private fun resolve(
        requestedKey: DependencyKey,
        scope: DependencyScope,
        creating: MutableList<KClass<*>>,
    ): Any {
        scope.checkOpen()
        val key = findDependencyKey(requestedKey, scope)
        val owner =
            scopes[key]?.let { type ->
                scope.findScope(type) ?: error("열려 있지 않은 스코프입니다: ${type.name}")
            }
        (if (owner != null) owner.cached(key) else scope.findCached(key))?.let { return it }

        val implementation = bindings[key]
        if (implementation == null && key.qualifier != null) {
            error("등록된 의존성이 없습니다: ${key.type.qualifiedName} @${key.qualifier.simpleName}")
        }
        if (implementation == null && key.type.java.isInterface) {
            error("등록된 구현체가 없습니다: ${key.type.qualifiedName}")
        }
        val instance = create(implementation ?: key.type, owner ?: scope, creating)
        owner?.cache(key, instance)
        return instance
    }

    private fun findDependencyKey(
        requestedKey: DependencyKey,
        scope: DependencyScope,
    ): DependencyKey {
        if (requestedKey.qualifier != null) return requestedKey
        if (requestedKey in bindings || requestedKey in scopes || requestedKey in scope.keys()) return requestedKey

        val candidates =
            (bindings.keys + scopes.keys + scope.keys())
                .filter { it.type == requestedKey.type }
                .distinct()
        if (candidates.size > 1) {
            val qualifiers = candidates.mapNotNull { it.qualifier?.simpleName }.sorted()
            error(
                "${requestedKey.type.qualifiedName} 타입에 여러 의존성이 등록되어 있습니다. " +
                    "Qualifier를 지정해 주세요: ${qualifiers.joinToString()}",
            )
        }
        val qualifier = candidates.singleOrNull()?.qualifier
        if (qualifier != null) {
            error("${requestedKey.type.qualifiedName} 타입에 Qualifier를 지정해 주세요: ${qualifier.simpleName}")
        }
        return requestedKey
    }

    private fun List<Annotation>.findQualifier(): KClass<out Annotation>? {
        val qualifiers = filter { it.annotationClass.java.isAnnotationPresent(Qualifier::class.java) }
        require(qualifiers.size <= 1) { "Qualifier는 하나만 지정할 수 있습니다." }
        return qualifiers.singleOrNull()?.annotationClass
    }
}

class DependencyScope internal constructor(
    private val container: DependencyContainer,
    val type: ScopeType,
    private val parent: DependencyScope? = null,
) : AutoCloseable {
    private val instances = mutableMapOf<DependencyKey, Any>()
    private var closed = false

    fun openChild(type: ScopeType): DependencyScope {
        checkOpen()
        return DependencyScope(container, type, this)
    }

    fun <T : Any> register(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) {
        checkOpen()
        instances[DependencyKey(type, qualifier)] = instance
    }

    fun <T : Any> get(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T = container.get(type, qualifier, this)

    fun create(type: KClass<*>): Any = container.create(type, this)

    fun inject(target: Any) = container.inject(target, this)

    override fun close() {
        instances.clear()
        closed = true
    }

    internal fun checkOpen() {
        check(!closed) { "닫힌 스코프입니다: ${type.name}" }
        parent?.checkOpen()
    }

    internal fun findScope(type: ScopeType): DependencyScope? = if (this.type == type) this else parent?.findScope(type)

    internal fun cached(key: DependencyKey): Any? = instances[key]

    internal fun findCached(key: DependencyKey): Any? = instances[key] ?: parent?.findCached(key)

    internal fun cache(
        key: DependencyKey,
        instance: Any,
    ) {
        instances[key] = instance
    }

    internal fun keys(): Set<DependencyKey> = instances.keys + (parent?.keys() ?: emptySet())
}
