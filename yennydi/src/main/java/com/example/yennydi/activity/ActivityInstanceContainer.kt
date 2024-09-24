package com.example.yennydi.activity

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.yennydi.di.AbstractDependencyContainer

class ActivityInstanceContainer : AbstractDependencyContainer(), DefaultLifecycleObserver {
    override fun onDestroy(owner: LifecycleOwner) {
        clear()
        super.onDestroy(owner)
    }
}
