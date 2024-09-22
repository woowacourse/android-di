package com.woowa.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlin.reflect.full.createInstance

inline fun <reified T : ViewModel> getDIViewModelFactory(): ViewModelProvider.Factory {
    val instance = T::class.createInstance()
    ViewModelComponentManager.createComponent(T::class)
    injectViewModelComponentFields<T>(instance)
    removeInstancesOnCleared<T>(instance)
    return viewModelFactory {
        addInitializer(T::class) {
            instance
        }
    }
}
