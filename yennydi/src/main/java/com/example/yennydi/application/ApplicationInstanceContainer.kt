package com.example.yennydi.application

import androidx.lifecycle.DefaultLifecycleObserver
import com.example.yennydi.di.AbstractDependencyContainer

class ApplicationInstanceContainer : AbstractDependencyContainer(), DefaultLifecycleObserver {
    fun getDeferred() = deferred.entries.flatMap { it.value }
}
