package woowacourse.shopping.container

import kotlin.reflect.KClass

class LifecycleContainer {
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()

    internal fun <T : Any> saveInstance(clazz: KClass<out T>, instance: T) {
        instances[clazz] = instance
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <T : Any> getSavedInstanceOf(clazz: KClass<out T>): T? {
        println(instances.toString())
        println(clazz)
        println(instances[clazz])
        return instances[clazz] as? T
    }
}
