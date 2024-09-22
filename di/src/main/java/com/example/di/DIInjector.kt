package com.example.di

import com.example.di.annotation.Inject
import com.example.di.annotation.LifeCycle
import com.example.di.annotation.LifeCycleScope
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

object DIInjector {
    fun injectModule(
        module: DIModule,
        lifeCycleScope: LifeCycleScope,
    ) {
        DIContainer.addInstance(module::class, null, module)

        module::class.declaredFunctions.forEach { function ->
            val returnType = function.returnType.jvmErasure
            val constructor = returnType.primaryConstructor
            val qualifierType = function.findAnnotation<Qualifier>()?.type

            if (constructor == null) {
                handleNoConstructorFunction(module, function, qualifierType, lifeCycleScope)
            } else {
                handleConstructorFunction(returnType, qualifierType, lifeCycleScope)
            }
        }
    }

    private fun handleNoConstructorFunction(
        module: DIModule,
        function: KFunction<*>,
        qualifierType: QualifierType?,
        lifeCycleScope: LifeCycleScope,
    ) {
        val parameters =
            (
                    listOf(module) +
                            function.parameters.drop(1).map {
                                val parameterInstance =
                                    DIContainer.getInstance(it.type.jvmErasure, qualifierType)
                                parameterInstance
                            }
                    ).toTypedArray()

        val functionInstance = function.call(*parameters) ?: return
        val functionLifeCycleScope = function.findAnnotation<LifeCycle>()?.scope ?: return

        if (lifeCycleScope == functionLifeCycleScope) {
            DIContainer.addInstance(function.returnType.jvmErasure, qualifierType, functionInstance)
        }
    }

    private fun handleConstructorFunction(
        returnType: KClass<*>,
        qualifierType: QualifierType?,
        lifeCycleScope: LifeCycleScope,
    ) {
        val instance = createInstance(returnType)
        DIContainer.addInstance(returnType, qualifierType, instance)

        val injectParameters =
            returnType.primaryConstructor?.parameters
                ?.filter { it.hasAnnotation<Inject>() }
                ?.map { it.type.jvmErasure }
                ?: return

        injectParameters.forEach { parameter ->
            val parameterInstance = createInstance(parameter)
            val parameterLifeCycleScope = parameter.findAnnotation<LifeCycle>()?.scope ?: return
            if (lifeCycleScope == parameterLifeCycleScope) {
                DIContainer.addInstance(parameter, qualifierType, parameterInstance)
            }
        }
    }

    fun <T : Any> createInstance(modelClass: KClass<T>): T {
        val constructor =
            modelClass.primaryConstructor
                ?: return DIContainer.getInstance(
                    modelClass,
                    modelClass.findAnnotation<Qualifier>()?.type,
                )

        val parameters =
            constructor.parameters.associateWith { parameter ->
                val annotation = parameter.findAnnotation<Qualifier>()?.type
                DIContainer.getInstance(parameter.type.jvmErasure, annotation)
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
                val fieldValue = DIContainer.getInstance(type, annotation)
                field.set(instance, fieldValue)
            }
        }
    }

    fun releaseModule(
        moduleType: KClass<*>,
        lifeCycleScope: LifeCycleScope,
    ) {
        moduleType.declaredFunctions.forEach { function ->
            val returnType = function.returnType.jvmErasure
            val qualifierType = function.findAnnotation<Qualifier>()?.type
            val functionLifeCycleScope = function.findAnnotation<LifeCycle>()?.scope ?: return
            if (lifeCycleScope == functionLifeCycleScope) {
                DIContainer.removeInstance(returnType, qualifierType)
            }
        }
    }
}
