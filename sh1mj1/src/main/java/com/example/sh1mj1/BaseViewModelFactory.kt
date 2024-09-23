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
    private val dependencyInjector = DependencyInjector(appContainer)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = dependencyInjector.createInstance(modelClass)
        val viewModelScopeCOmponents = viewModel.second
        viewModel.first.addCloseable {
            viewModelScopeCOmponents.forEach {
                dependencyInjector.removeViewModelScopeComponent(
                    it.first,
                    it.second,
                )
            }
        }
        return viewModel.first
    }
}

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
