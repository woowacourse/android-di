package com.example.bbottodi.di.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bbottodi.di.inject.AutoDependencyInjector.inject

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return inject(modelClass.kotlin)
    }
}
