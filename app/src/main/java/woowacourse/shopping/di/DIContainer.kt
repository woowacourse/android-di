package woowacourse.shopping.di

import kotlin.reflect.KClass

class DIContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()
    private val providers = mutableMapOf<KClass<*>, () -> Any>()

    fun <T : Any> register(
        clazz: KClass<T>,
        creator: () -> T,
    ) {
        providers[clazz] = creator
    }

    fun <T : Any> get(clazz: KClass<T>): T {
        if (instances.containsKey(clazz)) return instances[clazz] as T
        val creator = providers[clazz] ?: throw Exception("No provider for ${clazz.simpleName}")
        val instance = creator()
        instances[clazz] = instance
        return instance as T
    }
}
