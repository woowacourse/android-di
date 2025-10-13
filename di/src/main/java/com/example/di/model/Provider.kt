package com.example.di.model

internal interface Provider<T : Any> {
    fun get(overrides: Map<BindingKey, Any>): T
}
