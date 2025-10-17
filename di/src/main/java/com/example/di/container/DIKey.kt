package com.example.di.container

import kotlin.reflect.KClass

data class DIKey(
    val clazz: KClass<*>,
    val qualifier: KClass<out Annotation>? = null,
)
