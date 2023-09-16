package com.mission.androiddi.component.activity

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.mission.androiddi.component.application.InjectableApplication
import com.woowacourse.bunadi.injector.Injector

class ActivityDependencyLifecycleObserver(
    private val injector: Injector,
    private val activity: InjectableActivity,
) : DefaultLifecycleObserver {

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        val key = activity.activityClazz.qualifiedName ?: return
        val injectableApplication = activity.application as InjectableApplication
        val activityInjectManager = injectableApplication.requireActivityInjectManager()

        when {
            activity.isChangingConfigurations -> activityInjectManager.saveInjector(key, injector)
            activity.isFinishing -> activityInjectManager.removeInjector(key)
        }
    }
}
