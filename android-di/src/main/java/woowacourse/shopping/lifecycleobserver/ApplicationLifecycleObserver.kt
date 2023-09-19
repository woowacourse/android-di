package woowacourse.shopping.lifecycleobserver

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import woowacourse.shopping.Injector

interface ApplicationLifecycleObserver {
    fun setupLifecycleObserver(lifecycle: Lifecycle, context: Context, injector: Injector)
}

class DefaultApplicationLifecycleObserver : DefaultLifecycleObserver, ApplicationLifecycleObserver {
    private lateinit var context: Context
    private lateinit var injector: Injector

    override fun setupLifecycleObserver(
        lifecycle: Lifecycle,
        context: Context,
        injector: Injector,
    ) {
        lifecycle.addObserver(this)
        this.context = context
        this.injector = injector
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        injector.addDependency(
            dependency = DEPENDENCY,
            clazz = Context::class,
            instance = context,
        )
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        if (this::injector.isInitialized) {
            injector.releaseDependency(DEPENDENCY)
        }
    }

    companion object {
        private const val DEPENDENCY = "ApplicationContainer"
    }
}
