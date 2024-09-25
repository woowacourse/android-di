package com.example.yennydi.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.yennydi.activity.DiActivity
import com.example.yennydi.application.DiApplication
import com.example.yennydi.di.DependencyProvider

@MainThread
inline fun <reified VM : DiViewModel> DiActivity.injectedViewModels(
    dependencyProvider: DependencyProvider,
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = {
        viewModelFactory {
            initializer {
                val viewModelInstanceContainer = ViewModelInstanceContainer()
                dependencyProvider.register(viewModelInstanceContainer)
                val viewModel = (application as DiApplication).injector.inject(VM::class, viewModelInstanceContainer)
                viewModel.inject(viewModelInstanceContainer)
                viewModel
            }
        }
    },
): Lazy<VM> {
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        factoryProducer ?: { defaultViewModelProviderFactory },
        { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras },
    )
}
