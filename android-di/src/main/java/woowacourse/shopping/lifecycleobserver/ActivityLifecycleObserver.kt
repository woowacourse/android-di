package woowacourse.shopping.lifecycleobserver

import android.app.Activity
import android.content.Context
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

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        injector.addDependency(
            dependency = RELEASED_DEPENDENCY.format(activity::class.simpleName),
            clazz = Context::class,
            instance = activity,
        )
        injector.injectFields(activity::class, activity)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        if (this::activity.isInitialized && this::injector.isInitialized) {
            if (activity.isFinishing) {
                releaseDependency(RETAINED_DEPENDENCY.format(activity::class.simpleName))
            }
            releaseDependency(RELEASED_DEPENDENCY.format(activity::class.simpleName))
        }
    }

    private fun releaseDependency(dependency: String) {
        injector.releaseDependency(dependency)
    }

    companion object {
        private const val RETAINED_DEPENDENCY = "Retained%sContainer"
        private const val RELEASED_DEPENDENCY = "Released%sContainer"
    }
}
