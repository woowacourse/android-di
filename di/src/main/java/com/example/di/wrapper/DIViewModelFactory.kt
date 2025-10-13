package com.example.di.wrapper

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.di.AppContainer
import com.example.di.model.BindingKey

class DIViewModelFactory(
    private val container: AppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val bindingKey = BindingKey.from(SavedStateHandle::class, null)
        return container.resolve(
            type = modelClass.kotlin,
            overrides = mapOf(bindingKey to extras.createSavedStateHandle()),
        )
    }
}
