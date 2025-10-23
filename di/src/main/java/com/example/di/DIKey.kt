package com.example.di

import kotlin.reflect.KClass

data class DIKey(
    val clazz: KClass<*>,
    val qualifier: KClass<out Annotation>? = null,
)
