package com.mission.androiddi.component.activity

import com.woowacourse.bunadi.cache.Cache
import com.woowacourse.bunadi.cache.DefaultCache
import com.woowacourse.bunadi.injector.Injector

object NonConfigurationActivityInjectorManager {
    private val injectors = HashMap<String, Injector>()

    fun saveInjector(key: String, injector: Injector) {
        injectors[key] = injector
    }

    fun removeInjector(key: String?) {
        injectors.remove(key)
    }

    fun getInjector(activity: InjectableActivity, key: String?): Injector = injectors[key] ?: run {
        val parentCache = activity.application
        if (parentCache is Cache) {
            return ActivityDependencyInjector(DefaultCache(parentCache))
        }
        ActivityDependencyInjector()
    }
}
