package com.woowa.di

import javax.inject.Qualifier
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

fun KCallable<*>.findQualifierClassOrNull(): KClass<out Annotation>? =
    this.annotations.firstOrNull { annotation ->
        annotation.annotationClass.findAnnotation<Qualifier>() != null
    }?.annotationClass

fun KParameter.findQualifierClassOrNull(): KClass<out Annotation>? =
    this.annotations.firstOrNull { annotation ->
        annotation.annotationClass.findAnnotation<Qualifier>() != null
    }?.annotationClass
