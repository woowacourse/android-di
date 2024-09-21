package com.example.di

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class Injector(
    private val dependencyModule: DependencyProvider,
) {
    fun <T : Any> inject(modelClass: KClass<T>): T {
        val instance = createInstance(modelClass)
        injectProperty(modelClass, instance)
        return instance
    }

    private fun <T : Any> createInstance(modelClass: KClass<T>): T {
        val constructor =
            requireNotNull(modelClass.primaryConstructor) { "No suitable constructor found for $modelClass" }
        val arguments =
            constructor.parameters
                .filter { it.hasAnnotation<Injected>() }
                .associateWith { findParameterDependency(it) }

        return if (arguments.isEmpty()) {
            dependencyModule.getInstance(modelClass) ?: modelClass.createInstance().also {
                dependencyModule.addInstanceDependency(modelClass to it)
            }
        } else {
            constructor.callBy(arguments).also {
                dependencyModule.addInstanceDependency(modelClass to it)
            }
        }
    }

    private fun findParameterDependency(parameter: KParameter): Any {
        val qualifier =
            parameter.annotations.find { it.annotationClass.hasAnnotation<Qualifier>() }
        val type = parameter.type.jvmErasure

        return dependencyModule.getInstance(type, qualifier)
            ?: inject(dependencyModule.getImplementationClass(type, qualifier))
    }

    private fun <T : Any> injectProperty(
        modelClass: KClass<T>,
        instance: T,
    ) {
        val properties =
            modelClass.declaredMemberProperties
                .filterIsInstance<KMutableProperty1<T, *>>()
                .filter { it.hasAnnotation<Injected>() }

        properties.forEach { property ->
            property.isAccessible = true
            property.setter.call(instance, findPropertyDependency(property))
        }
    }

    private fun findPropertyDependency(property: KProperty1<*, *>): Any {
        val qualifier = property.annotations.find { it.annotationClass.hasAnnotation<Qualifier>() }
        val type = property.returnType.jvmErasure

        return dependencyModule.getInstance(type, qualifier)
            ?: inject(dependencyModule.getImplementationClass(type, qualifier))
    }
}
