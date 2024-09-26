package com.example.yennydi.di

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
        dependencies.forEach { dependency ->
            createInstance(dependency, container).also { container.addInstance(dependency, it) }
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
            container.getInstance(modelClass) ?: modelClass.createInstance()
        } else {
            constructor.callBy(arguments).also {
                injectProperty(it, container)
            }
        }
    }

    private fun findParameterDependency(
        parameter: KParameter,
        container: DependencyContainer,
    ): Any {
        val qualifier = parameter.annotations.find { it.annotationClass.hasAnnotation<Qualifier>() }
        val type = parameter.type.jvmErasure
        return findDependency(type, qualifier, container)
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
        return findDependency(type, qualifier, container)
    }

    private fun findDependency(
        type: KClass<*>,
        qualifier: Annotation?,
        container: DependencyContainer,
    ): Any {
        container.getInstance<Any>(type, qualifier)?.let { return it }
        val implementationClass =
            container.getImplementationClass(type, qualifier)
                ?: return applicationDependencyContainer.getInstance(type, qualifier)
                    ?: error("해당 타입에 대한 의존성이 등록되지 않았습니다. ${type.simpleName}")

        return createInstance(implementationClass, container).also { container.addInstance(type, it) }
    }
}
