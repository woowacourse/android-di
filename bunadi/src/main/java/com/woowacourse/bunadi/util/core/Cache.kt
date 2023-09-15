package com.woowacourse.bunadi.util.core

import com.woowacourse.bunadi.injector.DependencyKey
import com.woowacourse.bunadi.module.Module
import com.woowacourse.bunadi.util.Dependency
import com.woowacourse.bunadi.util.ProviderFunction

data class Cache(
    private val cache: MutableMap<DependencyKey, Dependency?> = mutableMapOf(),
) {
    fun caching(module: Module, provider: ProviderFunction) {
        val dependencyKey = DependencyKey.createDependencyKey(provider)
        val dependency = provider.call(module)

        cache[dependencyKey] = dependency
    }

    fun caching(dependencyKey: DependencyKey, dependency: Dependency? = null) {
        cache[dependencyKey] = dependency
    }

    operator fun get(dependencyKey: DependencyKey): Dependency? {
        return cache[dependencyKey]
    }

    fun clear(): Cache {
        cache.clear()
        return copy(cache = mutableMapOf())
    }
}
