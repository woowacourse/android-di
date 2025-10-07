package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.cast
import kotlin.reflect.full.primaryConstructor

object DiContainer {
    private val instancePool: ConcurrentHashMap<KClass<*>, Any> = ConcurrentHashMap()

    private val implementationMappings: Map<KClass<*>, KClass<*>> =
        mapOf(
            CartRepository::class to DefaultCartRepository::class,
            ProductRepository::class to DefaultProductRepository::class,
        )

    fun <T : Any> getInstance(kClass: KClass<T>): T {
        if (ViewModel::class.java.isAssignableFrom(kClass.java)) {
            return createInstance(kClass)
        }

        val implementClass: KClass<out Any> = implementationMappings[kClass] ?: kClass

        instancePool[implementClass]?.let {
            return kClass.cast(it)
        }

        instancePool[kClass]?.let {
            return kClass.cast(it)
        }

        val newInstance = createInstance(kClass)

        instancePool[kClass] = newInstance
        instancePool[implementClass] = newInstance

        return kClass.cast(newInstance)
    }

    private fun <T : Any> createInstance(kClass: KClass<T>): T {
        val implementClass: KClass<out Any> = implementationMappings[kClass] ?: kClass
        val constructor: KFunction<Any> = implementClass.primaryConstructor ?: error("")

        val arguments: Map<KParameter, Any> =
            constructor.parameters.associateWith { param ->
                getInstance(param.type.classifier as? KClass<*> ?: error(""))
            }

        return kClass.cast(constructor.callBy(arguments))
    }
}
