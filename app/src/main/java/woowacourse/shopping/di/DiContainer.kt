package woowacourse.shopping.di

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.cast
import kotlin.reflect.full.primaryConstructor

object DiContainer {
    private val instancePool: MutableMap<KClass<*>, Any> = mutableMapOf()

    private val implementationMappings: MutableMap<KClass<*>, KClass<*>> = mutableMapOf(
        CartRepository::class to DefaultCartRepository::class,
        ProductRepository::class to DefaultProductRepository::class,
    )

    fun <T : Any> getInstance(kClass: KClass<T>): T {
        instancePool[kClass]?.let {
            return kClass.cast(it) as T
        }

        val implementClass: KClass<out Any> = implementationMappings[kClass] ?: kClass
        val constructor: KFunction<Any> = implementClass.primaryConstructor ?: error("")

        val arguments: Map<KParameter, Any> = constructor.parameters.associateWith { param ->
            getInstance(param.type.classifier as? KClass<*> ?: error(""))
        }

        val instance = constructor.callBy(arguments)
        instancePool[kClass] = instance

        return kClass.cast(instance) as T
    }
}
