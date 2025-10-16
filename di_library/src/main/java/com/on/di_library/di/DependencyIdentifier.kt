package com.on.di_library.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

data class DependencyIdentifier(
    val module: KClass<*>,
    val function: KFunction<*>,
    val qualifier: String?,
)
