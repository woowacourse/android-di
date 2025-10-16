package com.daedan.di.annotation

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
annotation class Inject(
    val name: String = "",
)
