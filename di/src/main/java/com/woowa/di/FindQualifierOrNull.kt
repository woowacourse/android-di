package com.woowa.di

import javax.inject.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation

fun KFunction<*>.findQualifierClassOrNull(): KClass<out Annotation>? =
    this.annotations.firstOrNull { annotation ->
        annotation.annotationClass.findAnnotation<Qualifier>() != null
    }?.annotationClass

fun KProperty<*>.findQualifierClassOrNull(): KClass<out Annotation>? =
    this.annotations.firstOrNull { annotation ->
        annotation.annotationClass.findAnnotation<Qualifier>() != null
    }?.annotationClass

fun KParameter.findQualifierClassOrNull(): KClass<out Annotation>? =
    this.annotations.firstOrNull { annotation ->
        annotation.annotationClass.findAnnotation<Qualifier>() != null
    }?.annotationClass
