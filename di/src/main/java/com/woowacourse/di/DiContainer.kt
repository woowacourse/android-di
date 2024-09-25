package com.woowacourse.di

import com.woowacourse.di.annotations.Inject
import com.woowacourse.di.annotations.hasQualifier
import com.woowacourse.di.annotations.qualifierAnnotation
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

class DiContainer(private val parentContainer: DiContainer? = null) {
    private val binds: MutableMap<KClass<*>, Any> = mutableMapOf()
    private val qualifierBinds: MutableMap<KClass<out Annotation>, Any> = mutableMapOf()

    fun <T : Any, I : T> bind(
        bindClassType: KClass<T>,
        implClassType: KClass<I>,
    ) {
        binds[bindClassType] = createInstance(implClassType)
    }

    fun <T : Any> bind(
        bindClassType: KClass<T>,
        instance: T,
    ) {
        binds[bindClassType] = instance
    }

    fun <T : Any> provide(
        bindClassType: KClass<out Annotation>,
        instance: T? = null,
    ) {
        qualifierBinds[bindClassType] = instance ?: throw IllegalArgumentException()
    }

    fun <T : Any> match(bindClassType: KClass<T>): T {
        val instance = binds[bindClassType] ?: parentContainer?.match(bindClassType)
        return instance as T
    }

    fun <T : Any> matchByQualifier(bindClassType: KClass<out Annotation>): T {
        val instance =
            qualifierBinds[bindClassType] ?: parentContainer?.matchByQualifier(bindClassType)
        return instance as T
    }

    private fun <T : Any> createInstance(clazz: KClass<T>): T {
        val constructor =
            clazz.constructors.firstOrNull { it.hasAnnotation<Inject>() }
                ?: clazz.primaryConstructor
                ?: throw IllegalArgumentException()

        val parameters =
            constructor.parameters.map { parameter ->
                if (!parameter.hasAnnotation<Inject>()) {
                    throw IllegalArgumentException()
                }

                val parameterType =
                    parameter.type.classifier as? KClass<*>
                        ?: throw IllegalArgumentException()

                if (parameter.annotations.hasQualifier()) {
                    val qualifier =
                        parameter.annotations.qualifierAnnotation()
                            ?: throw IllegalArgumentException()
                    matchByQualifier(qualifier)
                } else {
                    match(parameterType)
                }
            }

        return constructor.call(*parameters.toTypedArray())
    }

    fun <T : Any> injectFields(instance: T) {
        val clazz = instance::class

        checkInjectAnnotations(clazz)

        clazz.java.declaredFields.forEach { field ->
            if (field.isAnnotationPresent(Inject::class.java)) {
                field.isAccessible = true
                val fieldAnnotations = field.annotations.toList()

                val fieldInstance =
                    if (fieldAnnotations.hasQualifier()) {
                        val qualifier =
                            fieldAnnotations.qualifierAnnotation()
                                ?: throw IllegalArgumentException()
                        matchByQualifier(qualifier)
                    } else {
                        match(field.type.kotlin)
                    }
                field.set(instance, fieldInstance)
            }
        }
    }

    fun <T : Any> checkInjectAnnotations(clazz: KClass<T>) {
        clazz.constructors.forEach { constructor ->
            constructor.parameters.forEach { parameter ->
                if (!parameter.hasAnnotation<Inject>()) {
                    throw IllegalArgumentException()
                }
            }
        }
    }
}
