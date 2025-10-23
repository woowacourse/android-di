package com.example.di

import kotlin.reflect.KClass

data class CacheKey(
    val type: KClass<*>,
    val qualifier: KClass<out Annotation>?,
    val scope: KClass<out Annotation>?
)

object DIContainer {
    private val providers = mutableMapOf<Pair<KClass<*>, KClass<out Annotation>?>, () -> Any>()
    private val cache = mutableMapOf<CacheKey, Any>()

    fun <T : Any> register(type: KClass<T>, provider: () -> T) {
        val qualifier = type.annotations
            .firstOrNull { it.annotationClass.annotations.any { meta ->
                meta.annotationClass.simpleName == "Qualifier"
            } }
            ?.annotationClass

        providers[type to qualifier] = provider
    }

    inline fun <reified T : Any> get(): T {
        return get(T::class)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(type: KClass<T>): T {
        val qualifier = type.annotations
            .firstOrNull { it.annotationClass.annotations.any { meta ->
                meta.annotationClass.simpleName == "Qualifier"
            } }
            ?.annotationClass

        val provider = providers[type to qualifier]
            ?: throw IllegalStateException(
                "No provider for $type" + (qualifier?.let { " with qualifier $it" } ?: "")
            )

        val scopeAnnotation = type.annotations
            .firstOrNull { it.annotationClass in setOf(Singleton::class, ViewModelScope::class, ActivityScope::class) }
            ?.annotationClass

        val cacheKey = CacheKey(type, qualifier, scopeAnnotation)

        return if (scopeAnnotation != null) {
            cache.getOrPut(cacheKey) { provider() } as T
        } else {
            provider() as T
        }
    }

    fun clearScope(scopeAnnotation: KClass<out Annotation>) {
        cache.entries.removeIf { it.key.scope == scopeAnnotation }
    }

    fun clear() {
        cache.clear()
        providers.clear()
    }
}
