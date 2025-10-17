package com.example.di_v2

import androidx.lifecycle.ViewModel

abstract class ScopedViewModel : ViewModel() {
    lateinit var diContainer: DIContainer

    override fun onCleared() {
        super.onCleared()
        if (::diContainer.isInitialized) {
            diContainer.clearScope(this)
        }
    }
}
