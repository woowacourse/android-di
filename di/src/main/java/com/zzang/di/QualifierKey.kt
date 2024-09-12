package com.zzang.di

import kotlin.reflect.KClass

data class QualifierKey<T : Any>(
    val type: KClass<T>,
    val qualifier: KClass<out Annotation>?,
)
