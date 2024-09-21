package com.example.sh1mj1

import kotlin.reflect.KClass

data class ComponentKey(
    val clazz: KClass<*>,
    val qualifier: Qualifier? = null,
)
