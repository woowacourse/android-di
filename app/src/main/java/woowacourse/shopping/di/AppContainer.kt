package woowacourse.shopping.di

import kotlin.reflect.KClass

class AppContainer {
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getInstance(type: KClass<T>): T {
        if (!instances.containsKey(type)) {
            throw IllegalStateException("Instance of type $type is not registered.")
        }
        return instances[type] as T
    }

    fun <T : Any> addInstance(
        type: KClass<T>,
        instance: T,
    ) {
        if (instances.containsKey(type)) {
            throw IllegalArgumentException("Instance of type $type is already registered.")
        }
        instances[type] = instance
    }
}
