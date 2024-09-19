package com.example.di

import com.example.di.annotation.Qualifier
import kotlin.reflect.KClass

object DIContainer {
    private val instances: MutableMap<Pair<KClass<*>, Qualifier?>, Any> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getInstance(
        type: KClass<T>,
        qualifier: Qualifier? = null,
    ): T {
        return instances[Pair(type, qualifier)] as? T ?: DIInjector.createInstance(type)
    }

    fun <T : Any> addInstance(
        type: KClass<T>,
        instance: Any,
        qualifier: Qualifier? = null,
    ) {
        if (!instances.containsKey(Pair(type, qualifier))) {
            instances[Pair(type, qualifier)] = instance
        }
    }

    fun clear() {
        instances.clear()
    }
}
