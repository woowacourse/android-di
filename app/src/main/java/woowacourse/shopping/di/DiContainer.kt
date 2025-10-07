package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DiContainer {
    private val providers = mutableMapOf<KClass<*>, Lazy<Any>>()

    fun <T : Any> addProviders(
        kClazz: KClass<T>,
        provider: () -> T,
    ) {
        providers[kClazz] = lazy(LazyThreadSafetyMode.SYNCHRONIZED) { provider() }
    }

    fun <T : Any> getProvider(kClazz: KClass<T>): T {
        val lazyProvider = providers.getOrPut(kClazz) {
            lazy(LazyThreadSafetyMode.SYNCHRONIZED) { createInstance(kClazz) }
        }
        return lazyProvider.value as T
    }

    private fun <T : Any> createInstance(kClazz: KClass<T>): T {
        val constructor = kClazz.primaryConstructor ?: throw IllegalArgumentException()
        val arguments =
            constructor.parameters.associateWith {
                val parameterClass = it.type.classifier as KClass<*>
                val instanceLazy =
                    lazy(LazyThreadSafetyMode.SYNCHRONIZED) { createInstance(kClazz) }
                providers.computeIfPresent(parameterClass) { _, _ ->
                    instanceLazy
                }
                instanceLazy.value
            }
        return constructor.callBy(arguments)
    }
}
