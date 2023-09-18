package com.ki960213.sheath.extention

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.hasAnnotation

internal inline fun <reified T : Annotation> KAnnotatedElement.hasAnnotationOrHasAttachedAnnotation(): Boolean =
    this.hasAnnotation<T>() || this.annotations.any { it.annotationClass.hasAnnotation<T>() }
