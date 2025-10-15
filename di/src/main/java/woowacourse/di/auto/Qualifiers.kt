package woowacourse.di.auto

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation

fun findQualifier(property: KProperty1<*, *>): KClass<out Annotation>? = findQualifier(property.annotations)

private fun findQualifier(annotations: List<Annotation>): KClass<out Annotation>? =
    annotations
        .firstOrNull { it.annotationClass.findAnnotation<Qualifier>() != null }
        ?.annotationClass
