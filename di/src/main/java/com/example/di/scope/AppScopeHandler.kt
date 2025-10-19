package com.example.di.scope

import androidx.activity.ComponentActivity
import com.example.di.DependencyKey
import kotlin.reflect.KClass

object AppScopeHandler : ScopeHandler {
    override val scopeAnnotation: KClass<out Annotation> = AppScope::class
    private val cache = mutableMapOf<DependencyKey<*>, Any>()

    init {
        ScopeContainer.setHandler(scopeAnnotation, this)
    }

    override fun <T : Any> getOrCreate(
        key: DependencyKey<T>,
        activity: ComponentActivity?,
        factory: () -> T,
    ): T = cache.getOrPut(key) { factory() } as T
}
