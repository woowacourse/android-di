package com.example.di.model

import kotlin.reflect.KClass

internal class FactoryProvider<T : Any>(
    private val type: KClass<T>,
    private val factory: (Map<BindingKey, Any>) -> T,
    private val isSingleton: Boolean,
) : Provider<T> {
    @Volatile
    private var cached: T? = null

    override fun get(overrides: Map<BindingKey, Any>): T {
        if (!isSingleton) return factory(overrides)

        val cache = cached
        if (cache != null) return cache

        synchronized(this) {
            val again = cached
            if (again != null) return again
            val created = factory(overrides)
            cached = created
            return created
        }
    }
}
