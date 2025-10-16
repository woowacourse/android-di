package com.yrsel.di

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner

class DIViewModelFactory(
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null,
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <VM : ViewModel> create(
        key: String,
        modelClass: Class<VM>,
        handle: SavedStateHandle,
    ): VM {
        val instance = DependencyInjector.injectConstructor(modelClass)
        return DependencyInjector.injectFields(instance)
    }
}

inline fun <reified VM : ViewModel> dIViewModels(owner: ViewModelStoreOwner) =
    lazy {
        val factory = DIViewModelFactory(owner as SavedStateRegistryOwner)
        factory.create(VM::class.java)
    }
