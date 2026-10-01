package com.cksckckcks.di

import kotlin.reflect.KClass

@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier

data class BindingKey(
    val type: KClass<*>,
    val qualifier: KClass<out Annotation>?,
)
