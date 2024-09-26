package com.woowacourse.di

import kotlin.reflect.KClass

data class ClassQualifier(
    val kClass: KClass<*>,
    val qualifier: KClass<out Annotation>?,
)
