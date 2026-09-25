package io.github.firstwoosun.di

import kotlin.reflect.KClass

data class DependencyBinding(
    val type: KClass<*>,
    val implementation: KClass<*>,
    val qualifier: KClass<out Annotation>? = null,
)
