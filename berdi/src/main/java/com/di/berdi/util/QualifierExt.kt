package com.di.berdi.util

import com.di.berdi.annotation.Qualifier
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.findAnnotation

val KAnnotatedElement.qualifiedName get() = findAnnotation<Qualifier>()?.qualifiedName
