package com.example.di

import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequireInjection(
    val impl: KClass<out Any>,
    val scope: KClass<out Annotation>,
)
