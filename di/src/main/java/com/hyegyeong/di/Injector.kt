package com.hyegyeong.di

import com.hyegyeong.di.annotations.Inject
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

object Injector {
    lateinit var container: Container

    inline fun <reified T : Any> inject(): T {
        val instance = injectConstructor<T>()
        val properties: List<KProperty<*>> =
            T::class.declaredMemberProperties.filter { it.hasAnnotation<Inject>() }
        val propertyDependencies = provideFieldInstances(properties)
        injectField(instance, properties, propertyDependencies)
        return instance
    }

    inline fun <reified T : Any> injectConstructor(): T {
        val constructor = requireNotNull(T::class.primaryConstructor)
        val parameters: List<KParameter> =
            constructor.parameters.filter { it.hasAnnotation<Inject>() }
        val parameterDependencies = provideConstructorInstances(parameters)
        return constructor.callBy(parameterDependencies)
    }

    fun provideConstructorInstances(parameters: List<KParameter>): Map<KParameter, Any> {
        // Inject 어노테이션 붙은 파라미터들만 들어옴
        val dependencies: MutableMap<KParameter, Any> = mutableMapOf()
        parameters.forEach { parameter ->
            val annotation: Annotation? = parameter.annotations.firstOrNull { it != Inject() }
            val annotationType = AnnotationType(annotation, parameter.type.jvmErasure)
            dependencies[parameter] =
                container.getInstance(annotationType) ?: throw NoSuchElementException()
        }
        return dependencies
    }

    fun provideFieldInstances(properties: List<KProperty<*>>): Map<KProperty<*>, Any> {
        // Inject 어노테이션 붙은 파라미터들만 들어옴
        val dependencies: MutableMap<KProperty<*>, Any> = mutableMapOf()
        properties.forEach { property ->
            val annotation: Annotation? =
                property.annotations.firstOrNull { it.annotationClass != Inject().annotationClass }
            val annotationType = AnnotationType(annotation, property.returnType.jvmErasure)
            dependencies[property] =
                container.getInstance(annotationType) ?: throw NoSuchElementException()
        }
        return dependencies
    }

    inline fun <reified T> injectField(
        instance: T,
        properties: List<KProperty<*>>,
        propertyDependencies: Map<KProperty<*>, Any>
    ) {
        properties.forEach {
            val property = it as KMutableProperty<*>
            property.isAccessible = true
            property.setter.call(instance, propertyDependencies[it])
        }
    }
}