package woowacourse.shopping.hashdi

import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class ActivityLifecycleTracker : DefaultLifecycleObserver {
    override fun onDestroy(owner: LifecycleOwner) {
        if (!(owner as ComponentActivity).isChangingConfigurations) {
            val injector = (owner.application as DiApplication).injector
            injector.removeActivityContainer(owner::class.simpleName.toString())
        }
        super.onDestroy(owner)
    }
}
