package com.woowa.di

import javax.inject.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation

fun KFunction<*>.findQualifierClassOrNull(): KClass<*>? =
    this.annotations.firstOrNull { annotation ->
        annotation.annotationClass.findAnnotation<Qualifier>() != null
    }?.annotationClass

fun KProperty<*>.findQualifierClassOrNull(): KClass<out Annotation>? =
    this.annotations.firstOrNull { annotation ->
        annotation.annotationClass.findAnnotation<Qualifier>() != null
    }?.annotationClass
