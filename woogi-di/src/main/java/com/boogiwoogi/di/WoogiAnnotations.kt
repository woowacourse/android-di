package com.boogiwoogi.di

import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class WoogiProperty

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class WoogiQualifier(val clazz: KClass<*>)
