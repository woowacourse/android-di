package woowacourse.shopping.ui.util

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class DependencyInjector(private val dependencyContainer: DependencyContainer) {
    fun <T : Any> createInstanceFromConstructor(modelClass: Class<T>): T {
        val kClass: KClass<T> = modelClass.kotlin
        val primaryConstructor = kClass.primaryConstructor
            ?: throw IllegalArgumentException("${kClass.java.simpleName}에 대한 주 생성자가 없음.")

        val constructorParams = primaryConstructor.parameters.map { param ->
            val kParamType = param.type.classifier as KClass<*>
            dependencyContainer.getInstance<Any>(kParamType)
        }.toTypedArray()

        return primaryConstructor.call(*constructorParams)
    }
}
