package woowacourse.shopping.di.inject

import woowacourse.shopping.di.container.Container
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

class CustomInject(
    private val container: Container,
) {

    fun <T : Any> getInstance(kClass: Class<T>): T {
        return createInstance(kClass.kotlin)
    }

    private fun <T : Any> createInstance(kClass: KClass<T>): T {
        val constructors = kClass.primaryConstructor ?: throw IllegalArgumentException()

        val params = constructors.parameters
        val arg = params.map {
            checkInstance(it.type)
        }.toTypedArray()

        return constructors.call(*arg)
    }

    private fun checkInstance(type: KType): Any {
        return container::class.declaredMemberProperties.find { it.returnType == type }?.getter?.call(
            container,
        ) ?: throw IllegalArgumentException()
    }
}
