package woowacourse.shopping.di.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class ComponentLifecycleObserver(
    private val onFinish: () -> Unit,
) : DefaultLifecycleObserver {
    override fun onDestroy(owner: LifecycleOwner) {
        onFinish()
    }
}
