package com.woowa.di

import javax.inject.Qualifier
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

fun KAnnotatedElement.findQualifierClassOrNull(): KClass<out Annotation>? =
    this.annotations.firstOrNull { annotation ->
        annotation.annotationClass.findAnnotation<Qualifier>() != null
    }?.annotationClass
