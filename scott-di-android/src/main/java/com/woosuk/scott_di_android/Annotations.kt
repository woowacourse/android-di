package com.woosuk.scott_di_android

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Singleton

@Target(AnnotationTarget.FUNCTION)
annotation class ActivityScope

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ActivityContext

@Target(AnnotationTarget.FUNCTION)
annotation class ViewModelScope

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject

@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier
