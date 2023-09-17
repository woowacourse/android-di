package com.woowacourse.bunadi.injector

import com.woowacourse.bunadi.cache.Cache
import com.woowacourse.bunadi.cache.DefaultCache
import kotlin.reflect.KClass

abstract class Injector(
    val cache: Cache = DefaultCache(),
) {
    abstract fun <T : Any> inject(clazz: KClass<T>): T
    abstract fun <T : Any> injectMemberProperties(clazz: KClass<T>, instance: Any)

    fun caching(dependencyKey: DependencyKey, dependency: Any?) {
        cache.caching(dependencyKey, dependency)
    }

    fun clear() {
        cache.clear()
    }
}
