package com.example.bbottodi.di.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import com.example.bbottodi.di.DiActivity
import com.example.bbottodi.di.inject.AutoDependencyInjector.createInstance

inline fun <reified viewModel : ViewModel> DiActivity.viewModels(): Lazy<viewModel> {
    return ViewModelLazy(
        viewModel::class,
        { viewModelStore },
        {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return createInstance(
                        this@viewModels.container,
                        modelClass.kotlin,
                    )
                }
            }
        },
    )
}
