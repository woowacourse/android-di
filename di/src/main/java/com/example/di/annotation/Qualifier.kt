package com.example.di.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier(val injectedClassType: KClass<*>)
