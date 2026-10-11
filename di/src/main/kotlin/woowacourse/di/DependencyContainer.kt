package woowacourse.di

import java.lang.reflect.Modifier
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class DependencyContainer : ScopedDependencyResolver {
    private val instances: MutableMap<DependencyKey, Any> = mutableMapOf()
    private val factories: MutableMap<DependencyKey, FactoryRegistration> = mutableMapOf()
    private val resolutionContext: ThreadLocal<ResolutionContext> = ThreadLocal()

    @Synchronized
    fun <T : Any> registerInstance(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) {
        val key = DependencyKey(type, qualifier)
        factories.remove(key)
        instances[key] = instance
    }

    @Synchronized
    fun <T : Any> registerFactory(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        scope: ScopeKey? = null,
        factory: (DependencyContainer) -> T,
    ) {
        val key = DependencyKey(type, qualifier)
        instances.remove(key)
        factories[key] = FactoryRegistration(scope, factory)
    }

    override fun openScope(
        key: ScopeKey,
        id: String?,
    ): DependencyScope =
        DependencyScope.create(
            key = key,
            id = id ?: UUID.randomUUID().toString(),
        )

    fun <T : Any> get(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T =
        withResolutionContext(scope = null) { context ->
            val dependency = synchronized(this) { resolve(type, qualifier, context.path) }
            @Suppress("UNCHECKED_CAST")
            dependency as T
        }

    override fun <T : Any> getInScope(
        type: KClass<T>,
        scope: DependencyScope,
        qualifier: KClass<out Annotation>?,
    ): T =
        withResolutionContext(scope) { context ->
            val dependency = synchronized(this) { resolve(type, qualifier, context.path) }
            @Suppress("UNCHECKED_CAST")
            dependency as T
        }

    override fun <T : Any> create(type: KClass<T>): T = create(type, scope = null)

    override fun <T : Any> createInScope(
        type: KClass<T>,
        scope: DependencyScope,
    ): T = create(type, scope)

    private fun <T : Any> create(
        type: KClass<T>,
        scope: DependencyScope?,
    ): T =
        withResolutionContext(scope) { context ->
            val instance =
                synchronized(this) {
                    resolve(type, qualifier = null, context.path, cacheResult = false)
                }
            injectMembers(instance)
            @Suppress("UNCHECKED_CAST")
            instance as T
        }

    fun injectMembers(instance: Any) {
        var currentType: Class<*>? = instance.javaClass
        while (currentType != null && currentType != Any::class.java) {
            currentType.declaredFields
                .filter { it.isAnnotationPresent(Inject::class.java) }
                .forEach { field ->
                    check(!Modifier.isFinal(field.modifiers)) {
                        "@Inject 필드는 변경 가능한 필드여야 합니다: ${field.declaringClass.name}.${field.name}"
                    }
                    val qualifier = qualifierOf(field.annotations.asIterable())
                    field.isAccessible = true
                    field.set(instance, get(field.type.kotlin, qualifier))
                }
            currentType = currentType.superclass
        }
    }

    @Synchronized
    fun clear() {
        instances.clear()
        factories.clear()
    }

    private fun resolve(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
        path: MutableList<DependencyKey>,
        cacheResult: Boolean = true,
    ): Any {
        val key = selectKey(type, qualifier)
        val registration = factories[key]

        if (registration?.scope == null && cacheResult) {
            instances[key]?.let { return it }
        }

        val scope =
            registration?.scope?.let { requiredScopeKey ->
                val activeScope =
                    resolutionContext.get()?.scope
                        ?: error(
                            "의존성에 스코프가 필요합니다: ${key.displayName()} " +
                                "(${requiredScopeKey.value})",
                        )
                check(activeScope.key == requiredScopeKey) {
                    "다른 스코프에서 의존성을 요청했습니다: ${key.displayName()} " +
                        "(필요: ${requiredScopeKey.value}, 현재: ${activeScope.key.value})"
                }
                check(!activeScope.isClosed) {
                    "이미 종료된 스코프입니다: ${activeScope.key.value} (${activeScope.id})"
                }
                activeScope
            }

        if (scope != null && cacheResult) {
            return scope.getOrCreate(key) { createDependency(key, registration, path, cacheSingleton = false) }
        }
        if (registration?.scope == null && cacheResult) {
            return instances.getOrPut(key) { createDependency(key, registration, path, cacheSingleton = true) }
        }
        return createDependency(key, registration, path, cacheSingleton = false)
    }

    private fun createDependency(
        key: DependencyKey,
        registration: FactoryRegistration?,
        path: MutableList<DependencyKey>,
        cacheSingleton: Boolean,
    ): Any {
        val cycleStart = path.indexOf(key)
        check(cycleStart < 0) {
            val cycle =
                (path.subList(cycleStart, path.size) + key)
                    .joinToString(" -> ") { it.displayName() }
            "순환 의존성이 발견되었습니다: $cycle"
        }

        path.add(key)
        try {
            val instance = registration?.factory?.invoke(this) ?: createFromPrimaryConstructor(key, path)
            if (cacheSingleton && registration?.scope == null) instances[key] = instance
            return instance
        } finally {
            path.removeAt(path.lastIndex)
        }
    }

    private fun selectKey(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
    ): DependencyKey {
        val registrations = (instances.keys + factories.keys).filter { it.type == type }.distinct()

        if (qualifier == null) {
            if (registrations.size > 1) {
                val availableQualifiers = registrations.mapNotNull { it.qualifier }.joinToString { it.simpleName.orEmpty() }
                throw AmbiguousDependencyException(
                    "의존성이 모호합니다: ${type.qualifiedName}. Qualifier를 지정하세요. 사용 가능: $availableQualifiers",
                )
            }

            registrations.singleOrNull()?.let { registration ->
                check(registration.qualifier == null) {
                    "Qualifier가 필요한 의존성입니다: ${type.qualifiedName} (${registration.qualifier?.simpleName})"
                }
                return registration
            }

            return DependencyKey(type, qualifier = null)
        }

        val qualifiedKey = DependencyKey(type, qualifier)
        check(qualifiedKey in instances || qualifiedKey in factories) {
            "등록되지 않은 의존성입니다: ${qualifiedKey.displayName()}"
        }
        return qualifiedKey
    }

    private fun createFromPrimaryConstructor(
        key: DependencyKey,
        path: MutableList<DependencyKey>,
    ): Any {
        val constructor =
            key.type.primaryConstructor
                ?: error("의존성을 등록하지 않았고 주 생성자를 찾을 수 없습니다: ${key.type.qualifiedName}")

        val dependencies =
            constructor.parameters.map { parameter ->
                val dependencyType =
                    parameter.type.classifier as? KClass<*>
                        ?: error("의존성 타입을 찾을 수 없습니다: ${parameter.name}")
                val qualifier = qualifierOf(parameter.annotations)
                resolve(dependencyType, qualifier, path)
            }

        return constructor.call(*dependencies.toTypedArray())
    }

    private inline fun <T> withResolutionContext(
        scope: DependencyScope?,
        block: (ResolutionContext) -> T,
    ): T {
        if (scope != null) {
            check(!scope.isClosed) { "이미 종료된 스코프입니다: ${scope.key.value} (${scope.id})" }
        }
        val current = resolutionContext.get()
        if (current != null) {
            if (scope != null) {
                check(current.scope === scope) { "중첩된 의존성 해석에서 스코프를 바꿀 수 없습니다." }
            }
            return block(current)
        }

        val context = ResolutionContext(scope, mutableListOf())
        resolutionContext.set(context)
        return try {
            block(context)
        } finally {
            resolutionContext.remove()
        }
    }

    private fun qualifierOf(annotations: Iterable<Annotation>): KClass<out Annotation>? {
        val qualifiers =
            annotations
                .map { it.annotationClass }
                .filter { it.java.isAnnotationPresent(Qualifier::class.java) }
        check(qualifiers.size <= 1) { "주입 지점에는 Qualifier를 하나만 지정할 수 있습니다." }
        return qualifiers.singleOrNull()
    }

    private data class FactoryRegistration(
        val scope: ScopeKey?,
        val factory: (DependencyContainer) -> Any,
    )

    private data class ResolutionContext(
        val scope: DependencyScope?,
        val path: MutableList<DependencyKey>,
    )
}

class AmbiguousDependencyException(
    message: String,
) : IllegalStateException(message)

private data class DependencyKey(
    val type: KClass<*>,
    val qualifier: KClass<out Annotation>?,
) {
    fun displayName(): String =
        buildString {
            append(type.simpleName ?: type.toString())
            qualifier?.simpleName?.let {
                append(" @")
                append(it)
            }
        }
}

fun dependencyContainer(block: DependencyContainer.() -> Unit): DependencyContainer = DependencyContainer().apply(block)
