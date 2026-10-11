package woowacourse.di

import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class Container {
    internal data class Key(
        val type: KClass<*>,
        val qualifier: KClass<out Annotation>?,
    )

    private sealed interface Binding {
        val scope: ScopeType

        data class Instance(
            val value: Any,
        ) : Binding {
            override val scope: ScopeType = ScopeType.ROOT
        }

        data class Implementation(
            val type: KClass<*>,
            override val scope: ScopeType,
        ) : Binding
    }

    private val bindings = mutableMapOf<Key, Binding>()
    private val scopes = mutableSetOf<Scope>()

    val rootScope: Scope = Scope(this, ScopeType.ROOT, null)

    init {
        scopes += rootScope
    }

    fun openScope(
        type: ScopeType,
        parent: Scope = rootScope,
    ): Scope {
        require(type != ScopeType.ROOT) { "루트 스코프는 다시 열 수 없습니다." }
        require(parent.container === this) { "다른 컨테이너의 스코프를 부모로 사용할 수 없습니다." }
        parent.ensureOpen()
        return Scope(this, type, parent).also { scopes += it }
    }

    fun <T : Any> register(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) {
        checkQualifier(qualifier)
        val key = Key(type, qualifier)
        invalidate(key)
        bindings[key] = Binding.Instance(instance)
        rootScope.cache(key) { instance }
    }

    fun <T : Any> bind(
        type: KClass<T>,
        implementation: KClass<out T>,
        qualifier: KClass<out Annotation>? = null,
        scope: ScopeType = ScopeType.ROOT,
    ) {
        checkQualifier(qualifier)
        val key = Key(type, qualifier)
        invalidate(key)
        bindings[key] = Binding.Implementation(implementation, scope)
    }

    fun <T : Any> create(type: KClass<T>): T = create(rootScope, type)

    fun resolve(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Any = resolve(rootScope, type, qualifier)

    internal fun <T : Any> create(
        scope: Scope,
        type: KClass<T>,
    ): T = create(scope, type, mutableListOf())

    internal fun resolve(
        scope: Scope,
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Any = resolve(scope, type, qualifier, mutableListOf())

    internal fun closeScope(scope: Scope) {
        require(scope.container === this) { "다른 컨테이너의 스코프는 닫을 수 없습니다." }
        if (scope.isClosed) return

        scopes
            .filter { it.isDescendantOf(scope) }
            .sortedByDescending { it.depth() }
            .forEach { closeScope(it) }
        scope.markClosed()
        scopes.remove(scope)
    }

    private fun <T : Any> create(
        scope: Scope,
        type: KClass<T>,
        resolvingPath: MutableList<KClass<*>>,
    ): T {
        scope.ensureOpen()
        check(type !in resolvingPath) {
            "의존성: ${(resolvingPath + type).joinToString(" - ") { it.simpleName ?: it.toString() }}"
        }
        resolvingPath.add(type)
        try {
            val constructor =
                type.primaryConstructor
                    ?: throw IllegalArgumentException("$type: 주 생성자를 찾을 수 없습니다.")
            val arguments =
                constructor.parameters.map { parameter ->
                    val parameterType =
                        parameter.type.classifier as? KClass<*>
                            ?: throw IllegalArgumentException("$type: 생성자 매개변수 타입을 확인할 수 없습니다.")
                    resolve(scope, parameterType, qualifierOf(parameter.annotations), resolvingPath)
                }
            val instance = constructor.call(*arguments.toTypedArray())

            type.java.declaredFields
                .filter { it.isAnnotationPresent(Inject::class.java) }
                .forEach { field ->
                    field.isAccessible = true
                    field.set(instance, resolve(scope, field.type.kotlin, qualifierOf(field.annotations.asIterable()), resolvingPath))
                }
            return instance
        } finally {
            resolvingPath.removeAt(resolvingPath.lastIndex)
        }
    }

    private fun resolve(
        scope: Scope,
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
        resolvingPath: MutableList<KClass<*>>,
    ): Any {
        scope.ensureOpen()
        checkQualifier(qualifier)
        val key = bindingKey(type, qualifier)
        val binding = bindings[key]
        val instanceScopeType = binding?.scope ?: scope.type
        val instanceScope =
            scope.find(instanceScopeType)
                ?: throw IllegalStateException("${type.simpleName}: ${instanceScopeType.name} 스코프가 열려 있지 않습니다.")

        return instanceScope.cache(key) {
            when (binding) {
                is Binding.Instance -> binding.value
                is Binding.Implementation -> create(instanceScope, binding.type, resolvingPath)
                null -> {
                    if (type.java.isInterface || Modifier.isAbstract(type.java.modifiers)) {
                        throw IllegalArgumentException("$type: 등록된 구현체가 없습니다.")
                    }
                    create(instanceScope, type, resolvingPath)
                }
            }
        }
    }

    private fun bindingKey(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
    ): Key {
        if (qualifier != null) {
            val key = Key(type, qualifier)
            require(key in bindings) { "$type: ${qualifier.simpleName} Qualifier에 등록된 구현체가 없습니다." }
            return key
        }

        val candidates = bindings.keys.filter { it.type == type }
        require(candidates.size <= 1) { "$type: 구현체가 여러 개입니다. Qualifier를 지정해야 합니다." }
        return candidates.singleOrNull() ?: Key(type, null)
    }

    private fun invalidate(key: Key) {
        scopes.forEach { it.remove(key) }
    }

    private fun qualifierOf(annotations: Iterable<Annotation>): KClass<out Annotation>? {
        val qualifiers =
            annotations
                .map { it.annotationClass }
                .filter { annotation -> annotation.annotations.any { it is Qualifier } }
        require(qualifiers.size <= 1) { "주입 대상에 Qualifier를 둘 이상 지정할 수 없습니다." }
        return qualifiers.singleOrNull()
    }

    private fun checkQualifier(qualifier: KClass<out Annotation>?) {
        require(qualifier == null || qualifier.annotations.any { it is Qualifier }) {
            "$qualifier: @Qualifier 애노테이션이 필요합니다."
        }
    }
}
