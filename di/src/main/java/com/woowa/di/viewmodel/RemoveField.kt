package com.woowa.di.viewmodel

import androidx.lifecycle.ViewModel

inline fun <reified T : ViewModel> removeInstancesOnCleared(instance: T) {
    instance.addCloseable {
        ViewModelComponentManager.getComponentInstance(T::class).deleteAllDIInstance()
        ViewModelComponent.deleteInstance(T::class)
    }
}
