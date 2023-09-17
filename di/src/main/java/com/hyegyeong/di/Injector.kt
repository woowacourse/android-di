package com.hyegyeong.di

import com.hyegyeong.di.annotations.Inject
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance
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
        val autoAvailableParameters = parameters.filter { it.annotations.isEmpty() }
        val autoNotAvailableParameters = parameters.filter { it.annotations.isNotEmpty() }
        autoAvailableParameters.forEach {
            parameterDependencies[it] = it::class.createInstance()
        }
        autoNotAvailableParameters.forEach {
            //Container 의 인스턴스 제공 함수 중 에서 해당 KParameter 와 같은 타입을 찾고, 그 중에서 어노테이션이 같은 인스턴스를 찾는다.
            val kclass = it.type.jvmErasure
            val annotations = it.annotations
            // 내가 찾는 의존성 제공 함수
            val instance = findContainerInstances(kclass, annotations)
            parameterDependencies[it] = instance
        }
        return constructor.callBy(parameterDependencies)
    }

    fun findContainerInstances(kClass: KClass<*>, annotations: List<Annotation>): Any {
        val containerFunctions: Collection<KFunction<*>> = container::class.declaredFunctions
        val function = containerFunctions.first { function ->
            (function.returnType.jvmErasure == kClass) && (function.annotations.containsAll(
                annotations
            ))
        }
        val instances = mutableListOf<Any>()
        if (function.valueParameters.isNotEmpty()) {
            function.valueParameters.forEach {
                instances.add(findContainerInstances(it.type.jvmErasure, it.annotations))
            }
        }
        return function.call(container, * instances.toTypedArray())
            ?: throw IllegalArgumentException("해당 함수를 찾을 수 없습니다.")
    }

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