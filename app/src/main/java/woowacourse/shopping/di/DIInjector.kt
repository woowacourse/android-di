package woowacourse.shopping.di

import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

object DIInjector {
    fun injectModule(module: DIModule) {
        val functions = module::class.declaredFunctions
        functions.forEach { function ->
            val injectParameters =
                function.parameters.filter { parameter ->
                    parameter.hasAnnotation<Inject>()
                }.map { injectParameter ->
                    DIContainer.getInstance(injectParameter.type.jvmErasure)
                }.toTypedArray()

            val type = function.returnType.jvmErasure
            val instance = function.call(module, *injectParameters) ?: return@forEach

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
