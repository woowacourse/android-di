package com.woowacourse.di.annotations

import kotlin.reflect.KClass

fun List<Annotation>.hasQualifier(): Boolean = any { it.isQualifier() }

fun List<Annotation>.qualifierAnnotation(): KClass<out Annotation>? {
    return firstOrNull { it.isQualifier() }?.annotationClass
}

fun Annotation.isQualifier(): Boolean {
    return annotationClass.annotations.any { it.annotationClass == Qualifier::class }
}
