package com.example.di

@Target(
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FIELD,
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject
