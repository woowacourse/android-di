package com.boogiwoogi.di

import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class WoogiProperty

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class WoogiQualifier(val clazz: KClass<*>)

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class WoogiWholeLifeCycle

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class WoogiActivityContext

@Target(AnnotationTarget.CLASS)
annotation class WoogiActivity

@Target(AnnotationTarget.CLASS)
annotation class WoogiApplication

@Target(AnnotationTarget.CLASS)
annotation class WoogiViewModel

@Target(AnnotationTarget.CLASS)
annotation class Module

@Target(AnnotationTarget.CLASS)
annotation class UsableOn(val clazz: KClass<*>)

@Target(AnnotationTarget.FUNCTION)
annotation class Provides
