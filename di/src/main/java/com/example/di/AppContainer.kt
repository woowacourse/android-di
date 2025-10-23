package com.example.di

import kotlin.reflect.KClass

class AppContainer {
    private val providers = mutableMapOf<DIKey, Provider<*>>()
    private val singletonCache = mutableMapOf<DIKey, Any>()
    private val activityCache = mutableMapOf<DIKey, Any>()
    private val viewModelCache = mutableMapOf<DIKey, Any>()

    fun <T : Any> register(
        clazz: KClass<T>,
        scope: Scope = Scope.Singleton,
        factory: () -> T,
    ) {
        providers[DIKey(clazz)] = Provider(factory, scope)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(clazz: KClass<T>): T {
        val key = DIKey(clazz)
        val provider =
            providers[key] ?: throw IllegalArgumentException("${clazz.simpleName}가 등록되지 않았습니다.")

        return when (provider.scope) {
            Scope.Singleton ->
                singletonCache.getOrPut(key) { provider.factory() } as T

            Scope.Activity ->
                activityCache.getOrPut(key) { provider.factory() } as T

            Scope.ViewModel ->
                viewModelCache.getOrPut(key) { provider.factory() } as T
        }
    }

    fun clearActivityScope() {
        activityCache.clear()
    }
}

data class Provider<T : Any>(
    val factory: () -> T,
    val scope: Scope,
)
