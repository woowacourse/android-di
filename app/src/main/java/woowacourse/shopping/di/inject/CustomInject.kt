package woowacourse.shopping.di.inject

import woowacourse.shopping.di.container.Container
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

class CustomInject(
    private val container: Container,
) {

    inline fun <reified T : Any> getInstance(kClass: KClass<T>): T {
        return createInstance(kClass)
    }

    fun <T : Any> createInstance(kClass: KClass<T>): T {
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
