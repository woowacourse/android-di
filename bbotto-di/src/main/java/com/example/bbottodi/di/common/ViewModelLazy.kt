package com.example.bbottodi.di.common

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import com.example.bbottodi.di.DiActivity
import com.example.bbottodi.di.inject.AutoDependencyInjector

@MainThread
inline fun <reified viewModel : ViewModel> DiActivity.viewModel(): Lazy<viewModel> {
    return ViewModelLazy(
        viewModel::class,
        { viewModelStore },
        {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AutoDependencyInjector.inject(
                        this@viewModel.container,
                        modelClass.kotlin,
                    )
                }
            }
        },
    )
}
