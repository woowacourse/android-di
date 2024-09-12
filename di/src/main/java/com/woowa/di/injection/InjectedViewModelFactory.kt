package com.woowa.di.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory

inline fun <reified T : ViewModel> getInjectedViewModelFactory(): ViewModelProvider.Factory {
    return viewModelFactory {
        addInitializer(T::class) {
            createInjectedInstance(T::class)
        }
    }
}
