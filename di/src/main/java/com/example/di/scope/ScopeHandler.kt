package com.example.di.scope

import androidx.lifecycle.SavedStateHandle
import kotlin.reflect.KClass

interface ScopeHandler {
    fun <T : Any> getInstance(
        kClass: KClass<T>,
        savedStateHandle: SavedStateHandle? = null,
        context: Any? = null,
    ): T
}
