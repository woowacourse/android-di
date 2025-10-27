package com.example.di

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class ScopedLifecycleObserver(
    private val di: DIContainer,
    private val owner: Any,
) : DefaultLifecycleObserver {
    override fun onDestroy(owner: LifecycleOwner) {
        di.clearScope(this.owner)
    }
}
