package com.zzang.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = DependencyInjector.inject(modelClass.kotlin)
        DependencyInjector.injectDependencies(viewModel, owner = viewModel)
        return viewModel
    }
}
