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
        println("hodu")
        println("\nhodu: owner = $owner")
        println("hodu: ViewModel Lifecycle Observer onCreate")
        onCreate()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        println("hodu")
        println("\nhodu: owner = $owner")
        println("hodu: ViewModel Lifecycle Observer onDestroy")
        when (owner) {
            is ComponentActivity -> {
                println("hodu: onDestory of Activity")
                if (owner.isFinishing) {
                    println("hodu: Activity is finished")
                    onFinish()
                }
            }

            is Fragment -> {
                println("hodu: onDestory of Fragment")
                if (owner.isRemoving || owner.isDetached) {
                    println("hodu: Fragment is finished")
                    onFinish()
                }
            }
        }
    }
}
