package com.medandro.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

object ViewModelFactory {
    fun create(diContainer: DIContainer): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val viewModel: T = diContainer.getInstance(DependencyKey(modelClass.kotlin)) as T
                diContainer.injectFields(viewModel)
                return viewModel
            }
        }
    }
}
