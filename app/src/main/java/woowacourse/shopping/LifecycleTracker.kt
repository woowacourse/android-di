package woowacourse.shopping

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class LifecycleTracker: DefaultLifecycleObserver {

    private val dependencyInjector = ShoppingApplication.dependencyInjector

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        dependencyInjector.clearActivityInstances()
    }
}
