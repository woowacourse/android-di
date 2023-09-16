package com.mission.androiddi.component.application

import android.app.Application
import com.mission.androiddi.component.activity.NonConfigurationActivityInjectorManager
import com.woowacourse.bunadi.cache.Cache
import com.woowacourse.bunadi.cache.DefaultCache

open class InjectableApplication : Application(), Cache by DefaultCache() {
    private var activityInjectManager: NonConfigurationActivityInjectorManager? = null

    override fun onCreate() {
        super.onCreate()
        activityInjectManager = NonConfigurationActivityInjectorManager()
    }

    fun requireActivityInjectManager(): NonConfigurationActivityInjectorManager {
        return requireNotNull(activityInjectManager) {
            ACTIVITY_INJECT_MANAGER_NOT_INITIALIZED
        }
    }

    companion object {
        private const val ACTIVITY_INJECT_MANAGER_NOT_INITIALIZED =
            "[ERROR] ActivityInjectManager가 초기화되지 않았습니다."
    }
}
