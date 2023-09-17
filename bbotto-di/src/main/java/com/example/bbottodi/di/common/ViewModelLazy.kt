package com.example.bbottodi.di.common

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy

@MainThread
inline fun <reified viewModel : ViewModel> ComponentActivity.viewModel(): Lazy<viewModel> {
    return ViewModelLazy(
        viewModel::class,
        { viewModelStore },
        { ViewModelFactory },
    )
}
