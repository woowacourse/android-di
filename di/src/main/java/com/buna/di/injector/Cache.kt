package com.buna.di.injector

import kotlin.reflect.KFunction

class Cache(
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

    fun get(dependencyKey: DependencyKey): Any? {
        return cache[dependencyKey]
    }
}
