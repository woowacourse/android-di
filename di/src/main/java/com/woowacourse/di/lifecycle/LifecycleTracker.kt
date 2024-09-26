package com.woowacourse.di.lifecycle

import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.woowacourse.di.Injector
import com.woowacourse.di.Module

class LifecycleTracker(private val module: Module) : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        module.install()
        Injector.injectProperty(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)

        if (owner is ComponentActivity &&
            owner.isChangingConfigurations
        ) {
            return
        }
        module.clear()
    }
}
