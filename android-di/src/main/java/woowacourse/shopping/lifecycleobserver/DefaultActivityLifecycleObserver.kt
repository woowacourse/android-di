package woowacourse.shopping.lifecycleobserver

import android.app.Activity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import woowacourse.shopping.Injector

interface ActivityLifecycleObserver {
    fun setupLifecycleObserver(lifecycle: Lifecycle, activity: Activity, injector: Injector)
}

class DefaultActivityLifecycleObserver : DefaultLifecycleObserver, ActivityLifecycleObserver {
    private lateinit var activity: Activity
    private lateinit var injector: Injector

    override fun setupLifecycleObserver(
        lifecycle: Lifecycle,
        activity: Activity,
        injector: Injector,
    ) {
        lifecycle.addObserver(this)
        this.activity = activity
        this.injector = injector
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        if (this::activity.isInitialized && this::injector.isInitialized) {
            if (activity.isFinishing) {
                releaseDependency("Retained" + activity::class.simpleName + "Container")
            }
            releaseDependency("Released" + activity::class.simpleName + "Container")
        }
    }

    private fun releaseDependency(dependency: String) {
        injector.releaseDependency(dependency)
    }
}
