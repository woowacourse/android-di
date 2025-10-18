package com.medandro.di

import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class ActivityScopeManager(
    private val container: DIContainer,
    private val activity: ComponentActivity,
) : DefaultLifecycleObserver {
    override fun onDestroy(owner: LifecycleOwner) {
        if (owner == activity && !activity.isChangingConfigurations) {
            container.clearActivityScope(activity)
        }
        super.onDestroy(owner)
    }
}
