package com.ki960213.sheath.annotation

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class Qualifier(val value: String)
