package com.mission.androiddi.component.application

import android.app.Application
import com.mission.androiddi.component.activity.retain.ActivityInjectorManager
import com.woowacourse.bunadi.cache.Cache
import com.woowacourse.bunadi.cache.DefaultCache
import com.woowacourse.bunadi.injector.Injectable

open class InjectableApplication(
    override val cache: Cache = DefaultCache(),
) : Application(), Injectable {
    private var nonConfigurationActivityInjectManager: ActivityInjectorManager? = null

    override fun onCreate() {
        super.onCreate()
        nonConfigurationActivityInjectManager = ActivityInjectorManager()
    }

    fun requireActivityInjectManager(): ActivityInjectorManager {
        return requireNotNull(nonConfigurationActivityInjectManager) {
            "[ERROR] ActivityInjectManager가 초기화되지 않았습니다."
        }
    }
}
