package com.android.di.annotation

@Target(
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.CLASS,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD,
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject
