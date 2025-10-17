package woowacourse.shopping

import android.app.Activity
import android.app.Application
import android.os.Bundle
import woowacourse.peto.di.DependencyContainer
import woowacourse.peto.di.DependencyInjector
import woowacourse.peto.di.annotation.Scope

class App : Application(), Application.ActivityLifecycleCallbacks {
    private lateinit var dependencyContainer: DependencyContainer

    override fun onCreate() {
        super.onCreate()
        val appContainer = AppContainer(this)
        dependencyContainer = appContainer.dependencyContainer
        DependencyInjector.init(dependencyContainer)
        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(
        activity: Activity,
        bundle: Bundle?,
    ) {
        dependencyContainer.inject(activity, Scope.ACTIVITY)
    }

    override fun onActivityDestroyed(activity: Activity) {
        dependencyContainer.clearScope(Scope.ACTIVITY)
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivitySaveInstanceState(
        activity: Activity,
        p1: Bundle,
    ) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}
}
