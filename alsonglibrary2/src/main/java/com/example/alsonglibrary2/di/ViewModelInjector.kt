package com.example.alsonglibrary2.di

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

inline fun <reified VM : ViewModel> ComponentActivity.createAutoDIViewModel(): Lazy<VM> {
    return viewModels {
        viewModelFactory {
            initializer {
                AutoDIManager.createAutoDIInstance<VM>()
            }
        }
    }
}
