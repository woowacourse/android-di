package olive.di

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class DIActivityLifecycleTracker : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        activityInstances.forEach { (type, instanceProvider) ->
            println("TEST!!!!!!!!!!!!!!! $type")
            val instance = instanceProvider.get()
            instances[type] = instance
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        activityInstances.forEach { (type, _) ->
            instances.remove(type)
        }
    }
}
