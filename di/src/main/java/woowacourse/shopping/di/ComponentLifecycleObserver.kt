package woowacourse.shopping.di

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class ComponentLifecycleObserver(
    private val onFinish: () -> Unit,
) : DefaultLifecycleObserver {
    override fun onDestroy(owner: LifecycleOwner) {
        println("hodu")
        println("\nhodu: owner = $owner")
        println("hodu: Component Lifecycle Observer onDestroy")
        onFinish()
    }
}
