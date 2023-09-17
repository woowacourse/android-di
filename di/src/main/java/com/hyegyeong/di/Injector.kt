package com.hyegyeong.di

import com.hyegyeong.di.annotations.Inject
import com.hyegyeong.di.annotations.Qualifier
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

object Injector {
    lateinit var container: DependencyContainer

    inline fun <reified T : Any> inject(): T {
        val constructor = requireNotNull(T::class.primaryConstructor)
        val instance: T = if (constructor.hasAnnotation<Inject>()) {
            injectConstructor()
        } else {
            constructor.call()
        }
        return instance
//        val instance = injectConstructor<T>()
//        val properties: List<KProperty<*>> =
//            T::class.declaredMemberProperties.filter { it.hasAnnotation<Inject>() }
//        val propertyDependencies = provideFieldInstances(properties)
//        injectField(instance, properties, propertyDependencies)
//        return instance
    }

    inline fun <reified T : Any> injectConstructor(): T {
        val constructor = requireNotNull(T::class.primaryConstructor)
        val parameters = constructor.parameters
        val parameterDependencies: MutableMap<KParameter, Any> = mutableMapOf()
        val nonQualifierParameters = parameters.filter {
            it.annotations.none { annotation ->
                annotation.annotationClass.hasAnnotation<Qualifier>()
            }
        }
        val qualifierInstanceParameters = parameters.filter {
            it.annotations.any { annotation ->
                annotation.annotationClass.hasAnnotation<Qualifier>()
            }
        }
        nonQualifierParameters.forEach {
            val instance = findContainerInstances(it.type.jvmErasure)
            parameterDependencies[it] = instance
        }
        qualifierInstanceParameters.forEach {
            val annotation = filterQualifierAnnotation(it.annotations)
            val instance = findContainerInstances(annotation = annotation)
            parameterDependencies[it] = instance
        }
        return constructor.callBy(parameterDependencies)
    }

    fun filterQualifierAnnotation(annotations: List<Annotation>): Annotation? {
        return annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }
    }

    fun findContainerInstances(kClass: KClass<*>? = null, annotation: Annotation? = null): Any {
        val containerFunctions: Collection<KFunction<*>> = container::class.declaredFunctions
        lateinit var function: KFunction<*>
        if (kClass == null) {
            function = containerFunctions.first { it.annotations.contains(annotation) }
        }
        if (annotation == null) {
            function = containerFunctions.first { it.returnType.jvmErasure == kClass }
        }
        if (kClass != null && annotation != null) {
            function = containerFunctions.first {
                (it.returnType.jvmErasure == kClass) && it.annotations.contains(annotation)
            }
        }
        val instances = mutableListOf<Any>()
        if (function.valueParameters.isNotEmpty()) {
            function.valueParameters.forEach {
                instances.add(
                    findContainerInstances(
                        it.type.jvmErasure,
                        filterQualifierAnnotation(it.annotations)
                    )
                )
            }
        }
        return function.call(container, * instances.toTypedArray())
            ?: throw IllegalArgumentException("해당 함수를 찾을 수 없습니다.")
    }

//    fun findContainerInstances(kClass: KClass<*>? = null, annotation: Annotation? = null): Any {
//        val containerFunctions: Collection<KFunction<*>> = container::class.declaredFunctions
//        val function = containerFunctions.first { function ->
//            (function.returnType.jvmErasure == kClass) && (function.annotations.containsAll(
//                annotations
//            ))
//        }
//        val instances = mutableListOf<Any>()
//        if (function.valueParameters.isNotEmpty()) {
//            function.valueParameters.forEach {
//                instances.add(findContainerInstances(it.type.jvmErasure, it.annotations))
//            }
//        }
//        return function.call(container, * instances.toTypedArray())
//            ?: throw IllegalArgumentException("해당 함수를 찾을 수 없습니다.")
//    }

//    inline fun <reified T : Any> injectConstructor(): T {
//        val constructor = requireNotNull(T::class.primaryConstructor)
//        val parameters: List<KParameter> =
//            constructor.parameters.filter { it.hasAnnotation<Inject>() }
//        val parameterDependencies = provideConstructorInstances(parameters)
//        return constructor.callBy(parameterDependencies)
//    }

//    fun provideConstructorInstances(parameters: List<KParameter>): Map<KParameter, Any> {
//        // Inject 어노테이션 붙은 파라미터들만 들어옴
//        val dependencies: MutableMap<KParameter, Any> = mutableMapOf()
//        parameters.forEach { parameter ->
//            val annotation: Annotation? = parameter.annotations.firstOrNull { it != Inject() }
//            val annotationType = AnnotationType(annotation, parameter.type.jvmErasure)
//            dependencies[parameter] =
//                container.getInstance(annotationType) ?: throw NoSuchElementException()
//        }
//        return dependencies
//    }
//
//    fun provideFieldInstances(properties: List<KProperty<*>>): Map<KProperty<*>, Any> {
//        // Inject 어노테이션 붙은 파라미터들만 들어옴
//        val dependencies: MutableMap<KProperty<*>, Any> = mutableMapOf()
//        properties.forEach { property ->
//            val annotation: Annotation? =
//                property.annotations.firstOrNull { it.annotationClass != Inject().annotationClass }
//            val annotationType = AnnotationType(annotation, property.returnType.jvmErasure)
//            dependencies[property] =
//                container.getInstance(annotationType) ?: throw NoSuchElementException()
//        }
//        return dependencies
//    }
//
//    inline fun <reified T> injectField(
//        instance: T,
//        properties: List<KProperty<*>>,
//        propertyDependencies: Map<KProperty<*>, Any>
//    ) {
//        properties.forEach {
//            val property = it as KMutableProperty<*>
//            property.isAccessible = true
//            property.setter.call(instance, propertyDependencies[it])
//        }
//    }
}