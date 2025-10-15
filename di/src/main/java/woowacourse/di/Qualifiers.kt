package woowacourse.di

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation

fun findQualifier(property: KProperty1<*, *>): KClass<out Annotation>? = findQualifier(property.annotations)

private fun findQualifier(annotations: List<Annotation>): KClass<out Annotation>? =
    annotations
        .map { it.annotationClass }
        .firstOrNull { it.findAnnotation<Qualifier>() != null }
