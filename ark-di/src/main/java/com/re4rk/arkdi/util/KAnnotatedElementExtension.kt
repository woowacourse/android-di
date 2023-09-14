package com.re4rk.arkdi.util

import com.re4rk.arkdi.Qualifier
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.findAnnotation

internal val KAnnotatedElement.qualifier: Annotation?
    get() = this.annotations.firstOrNull { it.annotationClass.findAnnotation<Qualifier>() != null }
