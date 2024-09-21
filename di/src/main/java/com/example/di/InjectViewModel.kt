package com.example.di

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

inline fun <reified VM : ViewModel> injectViewModels(componentActivity: ComponentActivity): Lazy<VM> =
    componentActivity.viewModels {
        viewModelFactory {
            initializer {
                DIContainer.inject<VM>()
            }
        }
    }
