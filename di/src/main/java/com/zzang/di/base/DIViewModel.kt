package com.zzang.di.base

import androidx.lifecycle.ViewModel
import com.zzang.di.DIContainer
import com.zzang.di.DependencyInjector

abstract class DIViewModel : ViewModel() {
    init {
        injectDependencies()
    }

    protected open fun injectDependencies() {
        DependencyInjector.injectDependencies(this, this)
    }

    override fun onCleared() {
        super.onCleared()
        DIContainer.clearViewModelScopedInstances(this)
    }
}
