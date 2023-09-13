package com.buna.di.util.diAssistant

import com.buna.di.injector.DependencyKey
import com.buna.di.module.Module
import kotlin.reflect.KFunction

data class Cache(
    private val cache: MutableMap<DependencyKey, Any?> = mutableMapOf(),
) {
    fun caching(module: Module, provider: KFunction<*>) {
        val dependencyKey = DependencyKey.createDependencyKey(provider)
        val dependency = provider.call(module)

        cache[dependencyKey] = dependency
    }

    fun caching(dependencyKey: DependencyKey, dependency: Any? = null) {
        cache[dependencyKey] = dependency
    }

    operator fun get(dependencyKey: DependencyKey): Any? {
        return cache[dependencyKey]
    }

    fun clear(): Cache {
        cache.clear()
        return copy(cache = mutableMapOf())
    }
}
