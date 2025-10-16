package com.example.di_v2.annotation

@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.CONSTRUCTOR,
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject
