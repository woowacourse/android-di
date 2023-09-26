package com.di.berdi.util

import com.di.berdi.annotation.Inject
import com.di.berdi.annotation.Qualifier
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

internal val KAnnotatedElement.qualifiedName get() = findAnnotation<Qualifier>()?.qualifiedName

internal val KClass<*>.declaredInjectProperties: List<KProperty<*>>
    get() = declaredMemberProperties.filter { it.hasAnnotation<Inject>() }
