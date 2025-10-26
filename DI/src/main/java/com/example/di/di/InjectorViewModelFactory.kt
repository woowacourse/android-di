package com.example.di.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class InjectorViewModelFactory(
    private val dependencyInjector: DependencyInjector,
    private val scopeHolder: Any,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = dependencyInjector.inject(modelClass.kotlin, scopeHolder)
}
