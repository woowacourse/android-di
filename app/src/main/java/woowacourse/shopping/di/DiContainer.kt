package woowacourse.shopping.di

import woowacourse.shopping.di.Injector.inject
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DiContainer {
    private val providers = mutableMapOf<KClass<*>, Lazy<Any>>()
    private val bindings = mutableMapOf<KClass<*>, KClass<*>>()

    fun <T : Any> addProviders(
        kClazz: KClass<T>,
        provider: () -> T,
    ) {
        providers[kClazz] = lazy(LazyThreadSafetyMode.SYNCHRONIZED) { provider() }
    }

    internal inline fun <reified T : Any, reified R : T> bind() {
        bindings[T::class] = R::class
    }

    fun <T : Any> getProvider(kClazz: KClass<T>): T {
        providers[kClazz]?.let {
            return it.value as T
        }

        val concreteClazz = bindings[kClazz] ?: kClazz

        if (concreteClazz.java.isInterface) {
            throw IllegalArgumentException()
        }

        val lazyProvider = providers.getOrPut(concreteClazz) {
            lazy(LazyThreadSafetyMode.SYNCHRONIZED) { createInstance(concreteClazz) }
        }

        if (kClazz != concreteClazz) {
            providers[kClazz] = lazyProvider
        }

        return lazyProvider.value as T
    }

    private fun <T : Any> createInstance(kClazz: KClass<T>): T {
        val constructor = kClazz.primaryConstructor
            ?: throw IllegalArgumentException()
        val arguments =
            constructor.parameters.associateWith { parameter ->
                val parameterClass = parameter.type.classifier as KClass<*>
                getProvider(parameterClass)
            }
        val instance = constructor.callBy(arguments)
        inject(instance)
        return instance
    }
}
