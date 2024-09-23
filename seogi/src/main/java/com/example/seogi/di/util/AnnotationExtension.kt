package com.example.seogi.di.util

import com.example.seogi.di.annotation.DaoInstance
import com.example.seogi.di.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation

internal fun KParameter.getAnnotationIncludeQualifier() = annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }

internal fun <T> KProperty1<T, *>.getAnnotationIncludeQualifier() =
    annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }

internal fun KClass<*>.getAnnotationIncludeQualifier() = annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }

internal fun KFunction<*>.getAnnotationIncludeQualifier() = annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }

internal fun KFunction<*>.hasDaoInstanceAnnotation() = annotations.contains(DaoInstance())
