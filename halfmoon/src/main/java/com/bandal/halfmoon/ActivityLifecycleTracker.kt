package com.bandal.halfmoon

import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class ActivityLifecycleTracker : DefaultLifecycleObserver {
    override fun onDestroy(lifecycleOwner: LifecycleOwner) {
        if (!(lifecycleOwner as ComponentActivity).isChangingConfigurations) {
            val injector = (lifecycleOwner.application as HalfMoonApplication).injector
            injector.removeContainer(lifecycleOwner::class.simpleName.toString())
        }
        super.onDestroy(lifecycleOwner)
    }
}
