package com.woowacourse.di

import javax.inject.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

typealias Instance = Any

object DependencyContainer {
    private val instances = mutableMapOf<ClassQualifier, Instance>()
    private const val CONSTRUCTOR_NOT_FOUND = "적합한 생성자를 찾을 수 없습니다."

    fun <T : Any> addInstance(
        kClass: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) {
        val classWithQualifier = ClassQualifier(kClass, qualifier)
        instances[classWithQualifier] = instance
    }

    private fun <T : Any> findInstance(
        kClass: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        val classWithQualifier = ClassQualifier(kClass, qualifier)
        return instances[classWithQualifier] as? T ?: createInstance(kClass)
    }

    fun <T : Any> createInstance(kClass: KClass<T>): T {
        val constructor = kClass.primaryConstructor ?: throw IllegalArgumentException(CONSTRUCTOR_NOT_FOUND)
        val dependencies: List<Any?> = constructor.extractDependencies()
        val instance = constructor.call(*dependencies.toTypedArray())

        injectFields(instance)

        return instance
    }

    private fun <T : Any> KFunction<T>.extractDependencies(): List<Any?> {
        return parameters.map { parameter ->
            val classifier: KClass<*> = parameter.type.classifier as KClass<*>
            findInstance(classifier)
        }
    }

    private fun <T : Any> injectFields(instance: T) {
        val properties =
            instance::class.declaredMemberProperties.filter { kProperty ->
                kProperty.hasAnnotation<Inject>()
            }

        properties.forEach { kProperty ->
            val classifier: KClass<*> = kProperty.returnType.classifier as KClass<*>
            val qualifier: Annotation? = kProperty.annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }
            val qualifierClass: KClass<out Annotation>? = qualifier?.annotationClass

            val dependency = findInstance(classifier, qualifierClass)
            kProperty as KMutableProperty1
            kProperty.setter.call(instance, dependency)
        }
    }

    fun clearViewModelInstances() {
        val viewmodelInstances = instances.filter { it.key.kClass.hasAnnotation<ViewModelScope>() }
        viewmodelInstances.forEach {
            instances.remove(it.key)
        }
    }

    fun clearActivityInstances() {
        val activityInstances = instances.filter { it.key.kClass.hasAnnotation<ActivityScope>() }
        activityInstances.forEach {
            instances.remove(it.key)
        }
    }
}
