package com.woowacourse.bunadi.cache

import com.woowacourse.bunadi.injector.DependencyKey
import com.woowacourse.bunadi.module.Module
import com.woowacourse.bunadi.util.Dependency
import com.woowacourse.bunadi.util.ProviderFunction

data class DefaultCache(
    private val parentCache: Cache? = null,
    private val cache: MutableMap<DependencyKey, Dependency?> = mutableMapOf(),
) : Cache {
    override fun caching(module: Module, provider: ProviderFunction) {
        val dependencyKey = DependencyKey.createDependencyKey(provider)
        val dependency = provider.call(module)

        cache[dependencyKey] = dependency
    }

    override fun caching(dependencyKey: DependencyKey, dependency: Dependency?) {
        cache[dependencyKey] = dependency
    }

    override operator fun get(dependencyKey: DependencyKey): Dependency? {
        return cache[dependencyKey] ?: parentCache?.get(dependencyKey)
    }

    override fun clear(): Cache {
        cache.clear()
        return copy(cache = mutableMapOf())
    }
}
