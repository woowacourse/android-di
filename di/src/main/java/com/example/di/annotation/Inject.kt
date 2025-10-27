package com.example.di.annotation

@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.CONSTRUCTOR,
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject
