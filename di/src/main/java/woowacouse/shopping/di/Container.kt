package woowacouse.shopping.di

import kotlin.reflect.KClass

class Container {
    private val dependencies = mutableMapOf<DependencyKey, Any>()

    fun <T : Any> registerInstances(
        kClass: KClass<T>,
        instance: T,
        qualifier: String? = null,
    ) {
        dependencies[DependencyKey(kClass, qualifier)] = instance
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(
        kClass: KClass<T>,
        qualifier: String? = null,
    ): T? = dependencies[DependencyKey(kClass, qualifier)] as? T
}
