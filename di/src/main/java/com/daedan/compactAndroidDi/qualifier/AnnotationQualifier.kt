package com.daedan.compactAndroidDi.qualifier

import kotlin.reflect.KClass

data class AnnotationQualifier(
    val annotation: KClass<out Annotation>,
) : Qualifier
