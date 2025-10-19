package com.example.di.scope

import androidx.lifecycle.SavedStateHandle
import kotlin.reflect.KClass

interface ScopeHandler {
    val scopeAnnotation: KClass<out Annotation>

    fun <T : Any> getInstance(
        kClass: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        savedStateHandle: SavedStateHandle? = null,
        context: Any? = null,
        hasScope: Boolean = false,
    ): T
}
