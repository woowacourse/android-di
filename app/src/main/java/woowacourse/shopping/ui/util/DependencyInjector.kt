package woowacourse.shopping.ui.util

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class DependencyInjector(private val dependencyContainer: DependencyContainer) {
    fun <T : Any> createInstanceFromConstructor(modelClass: Class<T>): T {
        val kClass: KClass<T> = modelClass.kotlin
        val primaryConstructor =
            requireNotNull(kClass.primaryConstructor) {
                "${kClass.java.simpleName} : $ERROR_MESSAGE_NO_PRIMARY_CONSTRUCTOR"
            }

        val constructorParams =
            primaryConstructor.parameters.map { param ->
                val kParamType = param.type.classifier as KClass<*>
                dependencyContainer.getInstance<Any>(kParamType)
            }.toTypedArray()

        return primaryConstructor.call(*constructorParams)
    }

    companion object {
        private const val ERROR_MESSAGE_NO_PRIMARY_CONSTRUCTOR = "해당 클래스의 주 생성자가 없습니다."
    }
}
