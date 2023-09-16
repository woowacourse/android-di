package com.di.berdi.util

import com.di.berdi.annotation.Qualifier
import kotlin.reflect.full.hasAnnotation

internal fun Annotation.hasQualifier() = annotationClass.hasAnnotation<Qualifier>()
