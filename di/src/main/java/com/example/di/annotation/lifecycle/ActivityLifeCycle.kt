package com.example.di.annotation.lifecycle

@LifeCycle
@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.VALUE_PARAMETER,
)
annotation class ActivityLifeCycle
