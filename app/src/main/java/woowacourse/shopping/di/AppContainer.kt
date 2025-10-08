package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.map
import kotlin.reflect.KClass

object AppContainer {
    private val bindings = mutableMapOf<KClass<*>, KClass<*>>()

    private val instances = ConcurrentHashMap<KClass<*>, Any>()

    init {
        register(CartRepository::class, CartRepositoryImpl::class)
        register(ProductRepository::class, ProductRepositoryImpl::class)
    }

    fun register(
        repository: KClass<*>,
        impl: KClass<*>,
    ) {
        bindings[repository] = impl
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(serviceClass: KClass<T>): T {
        val binding = bindings[serviceClass] ?: error("No binding for $serviceClass")
        val instance =
            instances.getOrPut(binding) {
                createInstance(binding)
            }
        return instance as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> createInstance(binding: KClass<T>): T {
        val args =
            binding.constructors
                .first()
                .parameters
                .map { parameter ->
                    val parameterClazz = parameter.type.classifier as KClass<*>
                    get(parameterClazz)
                }.toTypedArray()

        val called = binding.constructors.first().call(*args)
        return called
    }
}
