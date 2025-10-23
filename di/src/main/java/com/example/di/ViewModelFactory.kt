package com.example.di

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
        val instance: VM = DependencyInjector.instance(modelClass.kotlin)
        DependencyInjector.injectFields(instance)
        return instance
    }
}

inline fun <reified VM : ViewModel> ComponentActivity.injectableViewModel(): Lazy<VM> =
    ViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { viewModelStore },
        factoryProducer = { ViewModelFactory },
    )
