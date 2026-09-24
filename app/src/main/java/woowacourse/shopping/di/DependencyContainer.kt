package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class DependencyContainer {
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()
    private val factories: MutableMap<KClass<*>, (DependencyContainer) -> Any> = mutableMapOf()
    private val resolutionPath: ThreadLocal<MutableList<KClass<*>>> = ThreadLocal()

    @Synchronized
    fun <T : Any> registerInstance(
        type: KClass<T>,
        instance: T,
    ) {
        factories.remove(type)
        instances[type] = instance
    }

    @Synchronized
    fun <T : Any> registerFactory(
        type: KClass<T>,
        factory: (DependencyContainer) -> T,
    ) {
        instances.remove(type)
        factories[type] = factory
    }

    fun <T : Any> get(type: KClass<T>): T {
        val path =
            resolutionPath.get()
                ?: mutableListOf<KClass<*>>().also(resolutionPath::set)
        try {
            val dependency = synchronized(this) { resolve(type, path) }
            @Suppress("UNCHECKED_CAST")
            return dependency as T
        } finally {
            if (path.isEmpty()) resolutionPath.remove()
        }
    }

    @Synchronized
    fun clear() {
        instances.clear()
        factories.clear()
    }

    private fun resolve(
        type: KClass<*>,
        path: MutableList<KClass<*>>,
    ): Any {
        instances[type]?.let { return it }

        val cycleStart = path.indexOf(type)
        check(cycleStart < 0) {
            val cycle = (path.subList(cycleStart, path.size) + type).joinToString(" -> ") { it.simpleName ?: it.toString() }
            "순환 의존성이 발견되었습니다: $cycle"
        }

        path.add(type)
        try {
            val instance =
                factories[type]?.invoke(this)
                    ?: createFromPrimaryConstructor(type, path)
            instances[type] = instance
            return instance
        } finally {
            path.removeAt(path.lastIndex)
        }
    }

    private fun createFromPrimaryConstructor(
        type: KClass<*>,
        path: MutableList<KClass<*>>,
    ): Any {
        val constructor =
            type.primaryConstructor
                ?: error("의존성을 등록하지 않았고 주 생성자를 찾을 수 없습니다: ${type.qualifiedName}")

        val dependencies =
            constructor.parameters.map { parameter ->
                val dependencyType =
                    parameter.type.classifier as? KClass<*>
                        ?: error("의존성 타입을 찾을 수 없습니다: ${parameter.name}")
                resolve(dependencyType, path)
            }

        return constructor.call(*dependencies.toTypedArray())
    }
}
