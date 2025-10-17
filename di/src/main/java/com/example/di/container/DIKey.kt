package com.example.di.container

import kotlin.reflect.KClass

data class DIKey(
    val kClass: KClass<*>,
    val qualifierClass: KClass<*>? = null,
)
