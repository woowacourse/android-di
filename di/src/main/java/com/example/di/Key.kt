package com.example.di

import kotlin.reflect.KClass

data class Key(
    val type: KClass<*>,
    val qualifier: String?,
)
