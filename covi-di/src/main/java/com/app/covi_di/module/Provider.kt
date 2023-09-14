package com.app.covi_di.module

import kotlin.reflect.KClass

interface Provider {
    fun get(): Map<KClass<*>, Any>
}