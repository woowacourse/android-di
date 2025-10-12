package woowacourse.shopping

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.example.di.DependencyProvider
import woowacourse.shopping.di.RepositoryModule

class ShoppingApplication : Application() {
    val dependencyProvider by lazy { DependencyProvider(RepositoryModule(this)) }

    private val lifeCycleCallbacks =
        object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(
                activity: Activity,
                bundle: Bundle?,
            ) {
                dependencyProvider.injectViewModels(activity)
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
