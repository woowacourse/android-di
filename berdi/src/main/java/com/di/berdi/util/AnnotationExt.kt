package com.di.berdi.util

import com.di.berdi.annotation.Inject
import com.di.berdi.annotation.Qualifier
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

internal val KAnnotatedElement.qualifiedName get() = findAnnotation<Qualifier>()?.qualifiedName

internal fun <T : KAnnotatedElement> Collection<T>.filterInjectsProperties(): List<T> {
    return filter { it.hasAnnotation<Inject>() }
}
