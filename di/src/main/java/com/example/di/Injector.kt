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

class Injector(private val applicationDependencyContainer: DependencyContainer) {
    fun <T : Any> inject(
        modelClass: KClass<T>,
        container: DependencyContainer,
    ): T {
        val instance = createInstance(modelClass, container)
        injectProperty(instance, container)
        return instance
    }

    fun instantiateInstances(
        dependencies: List<KClass<out Any>>,
        container: DependencyContainer,
    ) {
        dependencies.forEach {
            createInstance(it, container)
        }
    }

    private fun <T : Any> createInstance(
        modelClass: KClass<T>,
        container: DependencyContainer,
    ): T {
        val constructor =
            requireNotNull(modelClass.primaryConstructor) { "No suitable constructor found for $modelClass" }
        val arguments =
            constructor.parameters
                .filter { it.hasAnnotation<Injected>() }
                .associateWith { findParameterDependency(it, container) }

        return if (arguments.isEmpty()) {
            container.getInstance(modelClass) ?: modelClass.createInstance().also {
                container.add(it)
            }
        } else {
            constructor.callBy(arguments).also {
                container.add(it)
            }
        }
    }

    private fun findParameterDependency(
        parameter: KParameter,
        container: DependencyContainer,
    ): Any {
        val qualifier =
            parameter.annotations.find { it.annotationClass.hasAnnotation<Qualifier>() }
        val type = parameter.type.jvmErasure

        return container.getInstance(type, qualifier) ?: applicationDependencyContainer.getInstance(type, qualifier)
            ?: createInstance(DependencyModule.getImplementationClass(type, qualifier), container)
    }

    fun <T : Any> injectProperty(
        instance: T,
        container: DependencyContainer,
    ) {
        val properties =
            instance::class.declaredMemberProperties
                .filterIsInstance<KMutableProperty1<T, *>>()
                .filter { it.hasAnnotation<Injected>() }

        properties.forEach { property ->
            property.isAccessible = true
            property.setter.call(instance, findPropertyDependency(property, container))
        }
    }

    private fun findPropertyDependency(
        property: KProperty1<*, *>,
        container: DependencyContainer,
    ): Any {
        val qualifier = property.annotations.find { it.annotationClass.hasAnnotation<Qualifier>() }
        val type = property.returnType.jvmErasure

        return container.getInstance(type, qualifier) ?: applicationDependencyContainer.getInstance(type, qualifier)
            ?: createInstance(DependencyModule.getImplementationClass(type, qualifier), container)
    }
}
