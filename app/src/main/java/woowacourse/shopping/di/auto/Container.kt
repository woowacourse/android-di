package woowacourse.shopping.di.auto

import kotlin.reflect.KClass

typealias Provider<T> = () -> T

open class Container {
    private val providers: MutableMap<KClass<*>, Provider<*>> = mutableMapOf()
    private val singletons: MutableMap<KClass<*>, Any> = mutableMapOf()

    fun <T : Any> bindSingleton(
        type: KClass<T>,
        creator: Provider<T>,
    ) {
        providers[type] = {
            singletons.getOrPut(type) { creator() }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(type: KClass<T>): T {
        val provider =
            providers[type]
                ?: error("No provider registered for ${type.qualifiedName}")
        return provider.invoke() as? T
            ?: error("Provider returned incorrect type for ${type.qualifiedName}")
    }

    inline fun <reified T : Any> get(): T = get(T::class)
}
