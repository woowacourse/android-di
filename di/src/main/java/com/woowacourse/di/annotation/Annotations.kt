package com.woowacourse.di.annotation

@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.PROPERTY,
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject

@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.PROPERTY,
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier(val value: String)
