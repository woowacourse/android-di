package com.woowacourse.di

import javax.inject.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

typealias QualifierClass = KClass<*>?
typealias Instance = Any
typealias ClassWithQualifier = Pair<KClass<*>, QualifierClass>

class DependencyInjector {
    private val instances = mutableMapOf<ClassWithQualifier, Instance>()

    fun <T : Any> addInstance(
        clazz: KClass<T>,
        instance: T,
        qualifier: KClass<*>? = null,
    ) {
        instances[clazz to qualifier] = instance
    }

    private fun <T : Any> findInstance(
        clazz: KClass<T>,
        qualifier: KClass<*>? = null,
    ): T {
        return instances[clazz to qualifier] as? T ?: createInstance(clazz)
    }

    fun <T : Any> createInstance(clazz: KClass<T>): T {
        val constructor = clazz.primaryConstructor ?: throw IllegalArgumentException(CONSTRUCTOR_NOT_FOUND)
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

    companion object {
        const val CONSTRUCTOR_NOT_FOUND = "적합한 생성자를 찾을 수 없습니다."
    }
}
