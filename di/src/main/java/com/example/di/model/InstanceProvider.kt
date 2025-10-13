package com.example.di.model

internal class InstanceProvider<T : Any>(
    private val instance: T,
) : Provider<T> {
    override fun get(overrides: Map<BindingKey, Any>): T = instance
}
