package com.yrsel.di

import java.util.concurrent.ConcurrentHashMap

object ScopeContainer {
    private val applicationScope = ConcurrentHashMap<DefinitionKey, Any>()
    private val activityScopes = ConcurrentHashMap<Any, MutableMap<DefinitionKey, Any>>()
    private val viewModelScopes = ConcurrentHashMap<String, MutableMap<DefinitionKey, Any>>()

    internal fun <T : Any> getOrCreate(
        key: DefinitionKey,
        scope: ScopeType,
    ): T {
        val map =
            when (scope) {
                is ScopeType.Activity -> activityScopes.getOrPut(scope.identifier) { mutableMapOf() }
                is ScopeType.ViewModel -> viewModelScopes.getOrPut(scope.identifier) { mutableMapOf() }
                is ScopeType.Singleton -> applicationScope
                is ScopeType.UnScoped -> mutableMapOf()
            }
        @Suppress("UNCHECKED_CAST")
        return map.getOrPut(key) {
            val value: ScopedProvider<Any> = DependencyContainer.get(key)
            value.provider.get()
        } as T
    }

    fun clear(scope: ScopeType) {
        when (scope) {
            is ScopeType.Activity -> activityScopes.remove(scope.identifier)
            is ScopeType.ViewModel -> viewModelScopes.remove(scope.identifier)
            else -> {}
        }
    }
}
