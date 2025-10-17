package com.m6z1.moongdi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.m6z1.moongdi.DependencyInjector.inject

inline fun <reified VM : ViewModel> injectedViewModelFactory(): ViewModelProvider.Factory =
    object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = inject<VM>() as T
    }
