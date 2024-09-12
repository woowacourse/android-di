package com.example.seogi.di

import kotlin.reflect.KClass

data class DependencyKey(
    val clazz: KClass<*>,
    val annotation: Annotation?,
)
