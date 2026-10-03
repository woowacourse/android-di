package woowacourse.di

import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class DependencyContainer : DependencyResolver {
    private val instances: MutableMap<DependencyKey, Any> = mutableMapOf()
    private val factories: MutableMap<DependencyKey, (DependencyContainer) -> Any> = mutableMapOf()
    private val resolutionPath: ThreadLocal<MutableList<DependencyKey>> = ThreadLocal()

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
        factory: (DependencyContainer) -> T,
    ) {
        val key = DependencyKey(type, qualifier)
        instances.remove(key)
        factories[key] = factory
    }

    fun <T : Any> get(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        val path =
            resolutionPath.get()
                ?: mutableListOf<DependencyKey>().also(resolutionPath::set)
        try {
            val dependency = synchronized(this) { resolve(type, qualifier, path) }
            @Suppress("UNCHECKED_CAST")
            return dependency as T
        } finally {
            if (path.isEmpty()) resolutionPath.remove()
        }
    }

    override fun <T : Any> create(type: KClass<T>): T {
        val path =
            resolutionPath.get()
                ?: mutableListOf<DependencyKey>().also(resolutionPath::set)
        try {
            val instance = synchronized(this) { resolve(type, qualifier = null, path, cacheResult = false) }
            injectMembers(instance)
            @Suppress("UNCHECKED_CAST")
            return instance as T
        } finally {
            if (path.isEmpty()) resolutionPath.remove()
        }
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
        if (cacheResult) instances[key]?.let { return it }

        val cycleStart = path.indexOf(key)
        check(cycleStart < 0) {
            val cycle =
                (path.subList(cycleStart, path.size) + key)
                    .joinToString(" -> ") { it.displayName() }
            "순환 의존성이 발견되었습니다: $cycle"
        }

        path.add(key)
        try {
            val instance =
                factories[key]?.invoke(this)
                    ?: createFromPrimaryConstructor(key, path)
            if (cacheResult) instances[key] = instance
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

    private fun qualifierOf(annotations: Iterable<Annotation>): KClass<out Annotation>? {
        val qualifiers =
            annotations
                .map { it.annotationClass }
                .filter { it.java.isAnnotationPresent(Qualifier::class.java) }
        check(qualifiers.size <= 1) { "주입 지점에는 Qualifier를 하나만 지정할 수 있습니다." }
        return qualifiers.singleOrNull()
    }
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
