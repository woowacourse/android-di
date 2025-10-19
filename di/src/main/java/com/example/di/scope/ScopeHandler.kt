package com.example.di.scope

import androidx.activity.ComponentActivity
import com.example.di.DependencyKey
import kotlin.reflect.KClass

interface ScopeHandler {
    val scopeAnnotation: KClass<out Annotation>

    fun <T : Any> getOrCreate(
        key: DependencyKey<T>,
        activity: ComponentActivity? = null,
        factory: () -> T,
    ): T
}
