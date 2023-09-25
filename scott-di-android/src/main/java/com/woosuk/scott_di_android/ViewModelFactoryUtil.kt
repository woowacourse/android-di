package com.woosuk.scott_di_android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object ViewModelFactoryUtil {
    inline fun <reified T : ViewModel> viewModelFactory(): ViewModelProvider.Factory =
        viewModelFactory {
            initializer {
                injectViewModel<T>()
            }
        }
}
