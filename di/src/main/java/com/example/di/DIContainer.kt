package com.example.di

import kotlin.reflect.KClass

data class CacheKey(
    val type: KClass<*>,
    val qualifier: KClass<out Annotation>,
    val scope: KClass<out Annotation>?
)

object DIContainer {
    val providers = mutableMapOf<Pair<KClass<*>, KClass<out Annotation>>, () -> Any>()
    private val cache = mutableMapOf<KClass<*>, Any>()

    fun register(type: KClass<*>, qualifier: KClass<out Annotation>, provider: () -> Any) {
        providers[type to qualifier] = provider
    }

    fun get(type: KClass<*>, qualifier: KClass<out Annotation>): Any {
        val provider = providers[type to qualifier]
            ?: throw IllegalArgumentException("No provider for $type with qualifier $qualifier")

        val scopeAnnotation = type.annotations
            .firstOrNull { it.annotationClass in setOf(Singleton::class, ViewModelScope::class, ActivityScope::class) }

        val cacheKey = CacheKey(type, qualifier, scopeAnnotation?.annotationClass)
        return cache.getOrPut(cacheKey.type) { provider() }
    }

    fun clearScope(scopeAnnotation: KClass<out Annotation>) {
        cache.entries.removeIf { it.key.annotations.any { a -> a.annotationClass == scopeAnnotation } }
    }
}
