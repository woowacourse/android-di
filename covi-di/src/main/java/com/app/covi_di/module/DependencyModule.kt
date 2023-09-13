package com.app.covi_di.module

import android.content.Context
import kotlin.reflect.KClass

interface DependencyModule {
    fun invoke(context: Context): Map<KClass<*>, KClass<*>>
}