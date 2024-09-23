package woowacourse.shopping.di

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class ComponentLifecycleObserver(
    private val onFinish: () -> Unit,
) : DefaultLifecycleObserver {
    override fun onDestroy(owner: LifecycleOwner) {
        Log.d("hodu", "Component Lifecycle Observer onDestroy")
        onFinish()
    }
}
