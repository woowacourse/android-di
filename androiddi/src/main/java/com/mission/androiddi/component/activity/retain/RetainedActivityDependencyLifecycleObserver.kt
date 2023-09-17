package com.mission.androiddi.component.activity.retain

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.mission.androiddi.component.activity.InjectableActivity
import com.mission.androiddi.component.application.InjectableApplication
import com.woowacourse.bunadi.injector.Injector

class RetainedActivityDependencyLifecycleObserver(
    private val injector: Injector,
    private val activity: InjectableActivity,
) : DefaultLifecycleObserver {

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        val injectorKey = activity.activityClazz.qualifiedName ?: return
        val injectableApplication = activity.application as InjectableApplication
        val injectManager = injectableApplication.requireActivityInjectManager()

        when {
            activity.isChangingConfigurations -> injectManager.saveInjector(injectorKey, injector)
            activity.isFinishing -> injectManager.removeInjector(injectorKey)
        }
    }
}
