package woowacourse.shopping.di

import kotlin.reflect.KClass

class Container {
    private val dependencies = mutableMapOf<DependencyKey, Any>()
    private val providers = mutableMapOf<DependencyKey, () -> Any>()

    fun <T : Any> registerInstances(
        kClass: KClass<T>,
        instance: T,
        qualifier: String? = null,
    ) {
        dependencies[DependencyKey(kClass, qualifier)] = instance
    }

    fun <T : Any> registerProvider(
        kClass: KClass<T>,
        qualifier: String? = null,
        provider: () -> T,
    ) {
        providers[DependencyKey(kClass, qualifier)] = provider
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(
        kClass: KClass<T>,
        qualifier: String? = null,
    ): T? = dependencies[DependencyKey(kClass, qualifier)] as? T
}
