package com.example.sh1mj1

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sh1mj1.container.AppContainer

class BaseViewModelFactory(
    appContainer: AppContainer,
) : ViewModelProvider.Factory {
    private val dependencyInjector = ViewModelDependencyInjector(appContainer)

    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
        val viewModelScopedInstanceWithKeys = dependencyInjector.viewModelScopedInstanceWithKeys(modelClass)
        return viewModelScopedInstanceWithKeys.viewModel
    }
}

// TODO: appContainer를 외부에서 주입하는 방식?
inline fun <reified VM : ViewModel> injectedViewModelFactory(): ViewModelProvider.Factory =
    viewModelFactory {
        initializer {
            val appContainer = (this[APPLICATION_KEY] as DiApplication).container
            BaseViewModelFactory(appContainer).create(VM::class.java)
        }
    }

inline fun <reified VM : ViewModel> ComponentActivity.injectedSh1mj1ViewModel(): Lazy<VM> =
    viewModels<VM> {
        injectedViewModelFactory<VM>()
    }

private const val TAG = "BaseViewModelFactory"