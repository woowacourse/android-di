package woowacourse.shopping.di

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class ViewModelLifecycleObserver(
    private val onCreate: () -> Unit,
    private val onFinish: () -> Unit,
) : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        onCreate()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        when (owner) {
            is ComponentActivity -> {
                if (owner.isFinishing) {
                    onFinish()
                }
            }

            is Fragment -> {
                if (owner.isRemoving || owner.isDetached) {
                    onFinish()
                }
            }
        }
    }
}
