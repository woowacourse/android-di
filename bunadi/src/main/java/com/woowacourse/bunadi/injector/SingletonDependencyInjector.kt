package com.woowacourse.bunadi.injector

import com.woowacourse.bunadi.annotation.Singleton
import com.woowacourse.bunadi.cache.Cache
import com.woowacourse.bunadi.cache.DefaultCache
import com.woowacourse.bunadi.module.Module
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberFunctions

class SingletonDependencyInjector(
    cache: Cache = DefaultCache(),
) : Injector(cache) {
    override val scopeAnnotation: KClass<out Annotation> = Singleton::class

    fun module(module: Module) {
        val providers = module::class.declaredMemberFunctions
        providers.forEach { provider -> cache.caching(module, provider) }
    }
}
