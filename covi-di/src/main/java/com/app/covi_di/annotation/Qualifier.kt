package com.app.covi_di.annotation

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier(val type: QualifierType)
