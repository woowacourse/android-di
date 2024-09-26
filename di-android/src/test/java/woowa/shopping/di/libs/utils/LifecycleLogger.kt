package woowa.shopping.di.libs.utils

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.test.core.app.ActivityScenario

class LifecycleLogger : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        println("onCreate")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        println("onStart")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        println("onResume")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        println("onPause")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        println("onStop")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        if (owner is Activity && owner.isChangingConfigurations) {
            println("onDestroy: configuration change")
        }
        println("onDestroy")
    }
}

inline fun <reified T : ComponentActivity> ActivityScenario<T>.addLogger(): ActivityScenario<T> {
    onActivity { activity ->
        activity.lifecycle.addObserver(LifecycleLogger())
    }
    return this
}
