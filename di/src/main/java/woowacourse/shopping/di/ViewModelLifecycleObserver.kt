package woowacourse.shopping.di

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class ViewModelLifecycleObserver(
    private val onFinish: () -> Unit,
) : DefaultLifecycleObserver {
    override fun onDestroy(owner: LifecycleOwner) {
        Log.d("hodu", "ViewModel Lifecycle Observer onDestroy")
        when (owner) {
            is ComponentActivity -> {
                Log.d("hodu", "onDestory of Activity")
                if (owner.isFinishing) {
                    Log.d("hodu", "Activity is finished")
                    onFinish()
                }
            }

            is Fragment -> {
                Log.d("hodu", "onDestory of Fragment")
                if (owner.isRemoving || owner.isDetached) {
                    Log.d("hodu", "Fragment is finished")
                    onFinish()
                }
            }
        }
    }
}
