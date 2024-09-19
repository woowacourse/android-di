package com.example.di

import com.example.di.annotation.Inject
import com.example.di.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

object DIInjector {
    fun injectModule(module: Module) {
        module::class.declaredFunctions.forEach { function ->
            val returnType = function.returnType.jvmErasure
            val constructor = returnType.primaryConstructor

            if (constructor == null) {
                handleNoConstructorFunction(module, function)
            } else {
                handleConstructorFunction(returnType)
            }
        }
    }

    private fun handleNoConstructorFunction(
        module: Module,
        function: KFunction<*>,
    ) {
        val parameters =
            (
                listOf(module) +
                    function.parameters.drop(1).map {
                        val parameterInstance =
                            DIContainer.getInstance(it.type.jvmErasure)
                        DIContainer.addInstance(parameterInstance::class, parameterInstance, null)
                        parameterInstance
                    }
            ).toTypedArray()

        val instance = function.call(*parameters) ?: return
        DIContainer.addInstance(function.returnType.jvmErasure, instance, null)
    }

    private fun handleConstructorFunction(returnType: KClass<*>) {
        val injectParameters =
            returnType.primaryConstructor?.parameters
                ?.filter { it.hasAnnotation<Inject>() }
                ?.map { it.type.jvmErasure }
                ?: return

        injectParameters.forEach { parameter ->
            val instance = createInstance(parameter)
            DIContainer.addInstance(parameter, instance, null)
        }
    }

    fun <T : Any> createInstance(modelClass: KClass<T>): T {
        val constructor = modelClass.primaryConstructor
        requireNotNull(constructor) { "Class must have a primary constructor: $modelClass" }

        val parameters =
            constructor.parameters.associateWith { parameter ->
                val annotation = parameter.findAnnotation<Qualifier>()
                if (annotation != null) {
                    val type = annotation.annotationClass
                    DIContainer.getInstance(type, annotation)
                } else {
                    val type = parameter.type.jvmErasure
                    DIContainer.getInstance(type)
                }
            }

        return constructor.callBy(parameters).also { injectFields(it) }
    }

    private fun <T : Any> injectFields(instance: T) {
        val properties =
            instance::class.declaredMemberProperties.filter { it.hasAnnotation<Inject>() }

        properties.forEach { property ->
            property.isAccessible = true
            property.javaField?.let { field ->
                val qualifier = field.getAnnotation(Qualifier::class.java)
                val fieldValue = DIContainer.getInstance(field.type.kotlin, qualifier)
                field.set(instance, fieldValue)
            }
        }
    }
}
