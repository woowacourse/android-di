package com.hyegyeong.di

import com.hyegyeong.di.annotations.Inject
import com.hyegyeong.di.annotations.Qualifier
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberProperties
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
        val properties: List<KProperty<*>> =
            T::class.declaredMemberProperties.filter { it.hasAnnotation<Inject>() }
        if (properties.isEmpty()) return instance
        return injectField(instance, properties)
    }

    inline fun <reified T : Any> injectConstructor(): T {
        val constructor = requireNotNull(T::class.primaryConstructor)
        val parameters = constructor.parameters
        val parameterDependencies: MutableMap<KParameter, Any> = mutableMapOf()
        val dependencies: Map<KClass<*>, Any> = findInstance(parameters.map { it.type.jvmErasure })
        for (parameter in parameters) {
            // parameter의 타입 (KType)을 가져오고, 그 타입에 해당하는 의존성을 dependencies에서 찾아서 추가
            val parameterType = parameter.type.jvmErasure
            val dependency = dependencies[parameterType]
            if (dependency != null) {
                parameterDependencies[parameter] = dependency
            }
        }
        return constructor.callBy(parameterDependencies)
    }

    fun findInstance(kclasss: List<KClass<*>>): MutableMap<KClass<*>, Any> {
        val dependencies: MutableMap<KClass<*>, Any> = mutableMapOf()
        val nonQualifierParameters = kclasss.filter {
            it.annotations.none { annotation ->
                annotation.annotationClass.hasAnnotation<Qualifier>()
            }
        }
        val qualifierInstanceParameters = kclasss.filter {
            it.annotations.any { annotation ->
                annotation.annotationClass.hasAnnotation<Qualifier>()
            }
        }
        nonQualifierParameters.forEach {
            val instance = findContainerInstances(it)
            dependencies[it] = instance
        }
        qualifierInstanceParameters.forEach {
            val annotation =
                it.annotations.firstOrNull { annotation -> annotation.annotationClass.hasAnnotation<Qualifier>() }
            val instance = findContainerInstances(annotation = annotation)
            dependencies[it] = instance
        }
        return dependencies
    }

    inline fun <reified T : Any> injectField(instance: T, properties: List<KProperty<*>>): T {
        // Inject 어노테이션 붙은 파라미터들만 들어옴
        val dependencies: Map<KClass<*>, Any> =
            findInstance(properties.map { it.returnType.jvmErasure })
        for (property in properties) {
            val propertyType = property.returnType.jvmErasure
            val dependency = dependencies[propertyType]
            property as KMutableProperty
            property.setter.call(instance, dependency)
        }
        return instance
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
                        it.annotations.firstOrNull { annotation -> annotation.annotationClass.hasAnnotation<Qualifier>() }
                    )
                )
            }
        }
        return function.call(container, * instances.toTypedArray())
            ?: throw IllegalArgumentException("해당 함수를 찾을 수 없습니다.")
    }

}