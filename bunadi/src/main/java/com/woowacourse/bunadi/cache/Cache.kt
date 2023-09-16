package com.woowacourse.bunadi.cache

import com.woowacourse.bunadi.injector.DependencyKey
import com.woowacourse.bunadi.module.Module
import com.woowacourse.bunadi.util.Dependency
import com.woowacourse.bunadi.util.ProviderFunction

interface Cache {
    fun caching(module: Module, provider: ProviderFunction)

    fun caching(dependencyKey: DependencyKey, dependency: Dependency? = null)

    operator fun get(dependencyKey: DependencyKey): Dependency?

    fun clear(): Cache
}
