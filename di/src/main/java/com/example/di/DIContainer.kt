package com.example.di

import kotlin.reflect.KClass

object DIContainer {
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getInstance(type: KClass<T>): T {
        return instances[type] as? T ?: DIInjector.createInstance(type)
    }

    fun <T : Any> addInstance(
        type: KClass<T>,
        instance: Any,
    ) {
        if (!instances.containsKey(type)) {
            instances[type] = instance
        }
    }

    fun clear() {
        instances.clear()
    }
}
