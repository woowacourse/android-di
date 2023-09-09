package com.buna.di.util.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

inline fun <reified VM : ViewModel> viewModelFactory(crossinline create: () -> VM): ViewModelProvider.Factory =
    viewModelFactory {
        initializer {
            create()
        }
    }
