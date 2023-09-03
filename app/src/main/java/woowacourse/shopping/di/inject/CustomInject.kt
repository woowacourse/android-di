package woowacourse.shopping.di.inject

import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.di.container.RepositoryContainer
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

object CustomInject {

    inline fun <reified T : Any> getInstance(kClass: KClass<T>): T {
        return createInstance(kClass)
    }
    fun <T : Any> createInstance(kClass: KClass<T>): T {
        val constructors = kClass.primaryConstructor ?: throw IllegalArgumentException()

        val paramsType = constructors.parameters
        val arg = paramsType.map {
            checkInstance(it.type)
        }.toTypedArray()

        return constructors.call(*arg.requireNoNulls())
    }

    private fun checkInstance(type: KType): Any? {
        return RepositoryContainer::class.declaredMemberProperties.find { it.returnType == type }
            ?.get(ShoppingApplication.repositoryContainer)
    }
}
