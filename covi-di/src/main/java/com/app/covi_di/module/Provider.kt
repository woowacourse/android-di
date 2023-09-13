package com.app.covi_di.module

import android.content.Context
import kotlin.reflect.KClass

interface Provider {
    fun init(context: Context)
    fun get(): Map<KClass<*>, Any>
}