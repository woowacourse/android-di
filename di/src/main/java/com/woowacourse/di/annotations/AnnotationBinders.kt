package com.woowacourse.di.annotations

import com.woowacourse.di.DiContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

fun List<Annotation>.hasQualifier(): Boolean = any { it.isQualifier() }

fun List<Annotation>.qualifierAnnotation(): KClass<out Annotation>? {
    return firstOrNull { it.isQualifier() }?.annotationClass
}

fun Annotation.isQualifier(): Boolean {
    return annotationClass.annotations.any { it.annotationClass == Qualifier::class }
}

fun <T : Any> injectFields(
    diContainer: DiContainer,
    instance: T,
) {
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
                    diContainer.matchByQualifier(qualifier)
                } else {
                    diContainer.match(field.type.kotlin)
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
