package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DIInjector {
    fun <T : Any> injectDependencies(modelClass: Class<T>): T {
        val constructor =
            modelClass.kotlin.primaryConstructor
                ?: throw IllegalArgumentException("Class must have a primary constructor: $modelClass")

        val parameters =
            constructor.parameters.map { parameter ->
                DIContainer.getInstance(parameter.type.classifier as KClass<*>)
            }.toTypedArray()

        return constructor.call(*parameters)
    }
}
