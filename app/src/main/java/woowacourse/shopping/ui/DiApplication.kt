package woowacourse.shopping.ui

import android.app.Activity
import android.app.Application
import android.os.Bundle
import woowacourse.shopping.di.DependencyProvider

class DiApplication : Application() {
    val lifeCycleCallbacks =
        object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(
                activity: Activity,
                bundle: Bundle?,
            ) {
                DependencyProvider.injectToActivity(activity)
            }

            override fun onActivityDestroyed(activity: Activity) = Unit

            override fun onActivityPaused(activity: Activity) = Unit

            override fun onActivityResumed(activity: Activity) = Unit

            override fun onActivitySaveInstanceState(
                activity: Activity,
                bundle: Bundle,
            ) = Unit

            override fun onActivityStarted(activity: Activity) = Unit

            override fun onActivityStopped(activity: Activity) = Unit
        }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(lifeCycleCallbacks)
    }
}
