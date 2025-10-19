package woowacourse.shopping

import RepositoryModule
import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.yrsel.di.ContextProvider
import com.yrsel.di.DependencyContainer
import com.yrsel.di.DependencyInjector
import com.yrsel.di.ScopeContainer
import com.yrsel.di.ScopeType
import woowacourse.shopping.di.DatabaseModule
import woowacourse.shopping.di.UtilModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DependencyContainer.init(
            RepositoryModule(),
            DatabaseModule(applicationContext),
            UtilModule(),
        )
        registerActivityLifecycle()
    }

    private fun registerActivityLifecycle() {
        registerActivityLifecycleCallbacks(
            object : ActivityLifecycleCallbacks {
                override fun onActivityCreated(
                    activity: Activity,
                    savedInstanceState: Bundle?,
                ) {
                    ContextProvider.register(activity)
                    DependencyInjector.injectFields(instance = activity, identifier = activity)
                }

                override fun onActivityDestroyed(activity: Activity) {
                    ContextProvider.unRegister(activity)
                    ScopeContainer.clear(ScopeType.Activity(activity))
                }

                override fun onActivityPaused(activity: Activity) {}

                override fun onActivityResumed(activity: Activity) {}

                override fun onActivitySaveInstanceState(
                    activity: Activity,
                    outState: Bundle,
                ) {
                }

                override fun onActivityStarted(activity: Activity) {}

                override fun onActivityStopped(activity: Activity) {}
            },
        )
    }
}
