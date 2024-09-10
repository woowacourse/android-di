package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

object DIInjector {
    fun injectModule(module: DIModule) {
        val functions = module::class.declaredFunctions
        functions.forEach { function ->
            val parameters =
                function.parameters.drop(1).map { parameter ->
                    DIContainer.getInstance(parameter.type.jvmErasure)
                }.toTypedArray()

            val type = function.returnType.jvmErasure
            val instance = function.call(module, *parameters) ?: return@forEach

            DIContainer.addInstance(type, instance)
        }
    }

    fun <T : Any> injectDependencies(modelClass: KClass<T>): T {
        val constructor =
            modelClass.primaryConstructor
                ?: throw IllegalArgumentException("Class must have a primary constructor: $modelClass")

        val parameters =
            constructor.parameters.map { parameter ->
                DIContainer.getInstance(parameter.type.classifier as KClass<*>)
            }.toTypedArray()

        return constructor.call(*parameters)
    }
}
