package com.example.di

import com.example.di.annotation.Inject
import com.example.di.annotation.Qualifier
import com.example.di.annotation.QualifierType
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

object Injector {
    fun injectModule(module: Module) {
        module::class.declaredFunctions.forEach { function ->
            val returnType = function.returnType.jvmErasure
            val constructor = returnType.primaryConstructor
            val qualifierType = function.findAnnotation<Qualifier>()?.type

            if (constructor == null) {
                handleNoConstructorFunction(module, function, qualifierType)
            } else {
                handleConstructorFunction(returnType, qualifierType)
            }
        }
    }

    private fun handleNoConstructorFunction(
        module: Module,
        function: KFunction<*>,
        qualifierType: QualifierType?,
    ) {
        val parameters =
            (
                listOf(module) +
                    function.parameters.drop(1).map {
                        val parameterInstance =
                            Container.getInstance(it.type.jvmErasure, qualifierType)
                                ?: return
                        Container.addInstance(
                            parameterInstance::class,
                            qualifierType,
                            parameterInstance,
                        )
                        parameterInstance
                    }
            ).toTypedArray()

        val instance = function.call(*parameters) ?: return
        Container.addInstance(function.returnType.jvmErasure, qualifierType, instance)
    }

    private fun handleConstructorFunction(
        returnType: KClass<*>,
        qualifierType: QualifierType?,
    ) {
        val injectParameters =
            returnType.primaryConstructor?.parameters
                ?.filter { it.hasAnnotation<Inject>() }
                ?.map { it.type.jvmErasure }
                ?: return

        injectParameters.forEach { parameter ->
            val instance = createInstance(parameter)
            Container.addInstance(parameter, qualifierType, instance)
        }
    }

    fun <T : Any> createInstance(modelClass: KClass<T>): T {
        val constructor =
            modelClass.primaryConstructor
                ?: return Container.getInstance(
                    modelClass,
                    modelClass.findAnnotation<Qualifier>()?.type,
                ) ?: throw IllegalArgumentException("Primary constructor not found")

        val parameters =
            constructor.parameters.associateWith { parameter ->
                val annotation = parameter.findAnnotation<Qualifier>()?.type
                Container.getInstance(parameter.type.jvmErasure, annotation)
            }

        return constructor.callBy(parameters).also { injectFields(it) }
    }

    fun <T : Any> injectFields(instance: T) {
        val properties =
            instance::class.declaredMemberProperties.filter { it.hasAnnotation<Inject>() }

        properties.forEach { property ->
            property.isAccessible = true
            property.javaField?.let { field ->
                val type = field.type.kotlin
                val annotation = property.findAnnotation<Qualifier>()?.type
                val fieldValue = Container.getInstance(type, annotation)
                field.set(instance, fieldValue)
            }
        }
    }
}
