package woowacourse.shopping.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference

object ActivityLifecycleManager : Application.ActivityLifecycleCallbacks {
    private var _currentActivity: WeakReference<Activity>? = null
    val currentActivity: Activity?
        get() = _currentActivity?.get()

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?,
    ) {
        ActivityScopeManager.onActivityCreated(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        _currentActivity = WeakReference(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        if (_currentActivity?.get() == activity) {
            _currentActivity?.clear()
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        ActivityScopeManager.onActivityDestroyed(activity)
    }

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle,
    ) {}
}
