package com.mission.androiddi.component.activity

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.woowacourse.bunadi.injector.Injector

class ActivityDependencyLifecycleObserver(
    private val injector: Injector,
    private val activity: InjectableActivity,
) : DefaultLifecycleObserver {

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        if (activity.isChangingConfigurations) {
            val key = activity.activityClazz.qualifiedName ?: return
            ActivityInjectorManager.saveInjector(key, injector)
            return
        }

        injector.clear()
    }
}
