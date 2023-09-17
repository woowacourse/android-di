package com.boogiwoogi.di

import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class Inject

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
annotation class Qualifier(val simpleName: String)

@Target(AnnotationTarget.CLASS)
annotation class Module

@Target(AnnotationTarget.CLASS)
annotation class UsableOn(val clazz: KClass<*>)

@Target(AnnotationTarget.FUNCTION)
annotation class Provides
