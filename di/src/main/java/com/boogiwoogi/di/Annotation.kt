package com.boogiwoogi.di

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class Inject

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
annotation class Qualifier(val simpleName: String)

@Target(AnnotationTarget.FUNCTION)
annotation class Provides

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
annotation class Scoped

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
annotation class Singleton
