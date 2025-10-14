package com.daedan.compactAndroidDi.annotation

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
annotation class Inject(
    val name: String = "",
)
