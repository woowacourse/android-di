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

        val newInstance = createInstance(kClass)
        instancePool[implementClass] = newInstance

        return kClass.cast(newInstance)
    }

    private fun <T : Any> createInstance(kClass: KClass<T>): T {
        val implementClass: KClass<out Any> = implementationMappings[kClass] ?: kClass
        val constructor: KFunction<Any> = implementClass.primaryConstructor
            ?: error(ERROR_MESSAGE_NOT_HAVE_DEFAULT_CONSTRUCTOR.format(implementClass.simpleName))

        val arguments: Map<KParameter, Any> =
            constructor.parameters.associateWith { param ->
                getInstance(
                    param.type.classifier as? KClass<*>
                        ?: error(
                            ERROR_MESSAGE_CANNOT_GET_INSTANCE.format(
                                implementClass.simpleName,
                                param
                            )
                        )
                )
            }

        return kClass.cast(constructor.callBy(arguments))
    }

    private const val ERROR_MESSAGE_NOT_HAVE_DEFAULT_CONSTRUCTOR = "%s 클래스에 기본 생성자가 없습니다."
    private const val ERROR_MESSAGE_CANNOT_GET_INSTANCE = "생성자 %s의 파라미터 %s 타입을 가져올 수 없습니다."
}
