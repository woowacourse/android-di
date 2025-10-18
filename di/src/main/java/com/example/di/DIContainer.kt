package com.example.di

import kotlin.reflect.KClass

object DIContainer {
    val providers = mutableMapOf<Pair<KClass<*>, KClass<out Annotation>>, () -> Any>()
    private val cache = mutableMapOf<KClass<*>, Any>()

    fun register(type: KClass<*>, qualifier: KClass<out Annotation>, provider: () -> Any) {
        providers[type to qualifier] = provider
    }

    fun get(type: KClass<*>, qualifier: KClass<out Annotation>): Any {
        return cache.getOrPut(type) {
            providers[type to qualifier]?.invoke()
                ?: throw IllegalArgumentException("No provider for $type with qualifier $qualifier")
        }
    }
}
