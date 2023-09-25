package com.hyegyeong.di

import com.hyegyeong.di.annotations.Inject
import com.hyegyeong.di.annotations.Qualifier
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

object Injector {

    inline fun <reified T : Any> inject(): T {
        val constructor = requireNotNull(T::class.primaryConstructor)
        val instance: T = if (constructor.hasAnnotation<Inject>()) {
            injectConstructor()
        } else {
            constructor.call()
        }
        val properties: List<KProperty<*>> =
            T::class.declaredMemberProperties.filter { it.hasAnnotation<Inject>() }
        if (properties.isEmpty()) return instance
        return injectField(instance, properties)
    }

    inline fun <reified T : Any> injectConstructor(): T {
        println("test 들어옴")
        val constructor = requireNotNull(T::class.primaryConstructor)
        val parameters = constructor.parameters
        val dependencies: Map<KParameter, Any> = findParametersInstances(parameters)
        return constructor.callBy(dependencies)
    }

    fun findParametersInstances(parameters: List<KParameter>): Map<KParameter, Any> {
        val dependencies: MutableMap<KParameter, Any> = mutableMapOf()
        val nonQualifierParameters = parameters.filterNot { parameter ->
            checkQualifierAnnotation(parameter.annotations)
        }
        val qualifierInstanceParameters = parameters.filter { parameter ->
            checkQualifierAnnotation(parameter.annotations)
        }
        nonQualifierParameters.forEach {
            val instance = DiContainer.provideInstance(it.type.jvmErasure, it.annotations)
            dependencies[it] = instance
        }
        qualifierInstanceParameters.forEach {
            val instance = DiContainer.provideInstance(
                it.type.jvmErasure,
                it.annotations
            )
            dependencies[it] = instance
        }
        return dependencies
    }

    private fun checkQualifierAnnotation(annotations: List<Annotation>): Boolean {
        val qualifierAnnotations =
            annotations.filter { it.annotationClass.hasAnnotation<Qualifier>() }
        return qualifierAnnotations.isNotEmpty()
    }

    fun findPropertyInstances(properties: List<KProperty<*>>): Map<KProperty<*>, Any> {
        val dependencies: MutableMap<KProperty<*>, Any> = mutableMapOf()
        val nonQualifierParameters = properties.filterNot { property ->
            checkQualifierAnnotation(property.annotations)
        }
        val qualifierInstanceParameters = properties.filter { property ->
            checkQualifierAnnotation(property.annotations)
        }
        nonQualifierParameters.forEach {
            val instance = DiContainer.provideInstance(it.returnType.jvmErasure, it.annotations)
            dependencies[it] = instance
        }
        qualifierInstanceParameters.forEach {
            val instance = DiContainer.provideInstance(
                it.returnType.jvmErasure,
                it.annotations
            )
            dependencies[it] = instance
        }
        return dependencies
    }

    inline fun <reified T : Any> injectField(instance: T, properties: List<KProperty<*>>): T {
        // Inject 어노테이션 붙은 파라미터들만 들어옴
        val dependencies: Map<KProperty<*>, Any> =
            findPropertyInstances(properties)
        for (property in properties) {
            val dependency = dependencies[property]
            property as KMutableProperty
            property.setter.call(instance, dependency)
        }
        return instance
    }
}
