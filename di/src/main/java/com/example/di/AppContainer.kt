package com.example.di

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class AppContainer(
    private val bindings: Map<DIKey, () -> Any> = emptyMap(),
) {
    private val singletonCache = mutableMapOf<DIKey, Any>()
    private val activityCache = mutableMapOf<DIKey, Any>()
    private val viewModelCache = mutableMapOf<DIKey, Any>()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(
        clazz: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        scope: Scope = Scope.Singleton,
    ): T {
        val key = DIKey(clazz, qualifier)
        return when (scope) {
            Scope.Singleton -> singletonCache.getOrPut(key) { createInstance(key) } as T
            Scope.Activity -> activityCache.getOrPut(key) { createInstance(key) } as T
            Scope.ViewModel -> viewModelCache.getOrPut(key) { createInstance(key) } as T
        }
    }

    private fun createInstance(key: DIKey): Any = bindings[key]?.invoke() ?: key.clazz.createInstance()

    fun clearActivityScope() {
        activityCache.clear()
    }

    fun clearViewModelScope() {
        viewModelCache.clear()
    }
}
