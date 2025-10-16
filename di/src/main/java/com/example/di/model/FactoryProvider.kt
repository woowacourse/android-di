package com.example.di.model

internal class FactoryProvider<T : Any>(
    private val factory: (Map<BindingKey, Any>) -> T,
) : Provider<T> {
    override fun get(overrides: Map<BindingKey, Any>): T = factory(overrides)
}
