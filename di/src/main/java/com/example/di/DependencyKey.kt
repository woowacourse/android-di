package com.example.di

import kotlin.reflect.KClass

data class DependencyKey<T : Any>(
    val type: KClass<T>,
    val qualifier: KClass<out Annotation>? = null,
)
