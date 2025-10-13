package com.example.di

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = DependencyInjector.create(modelClass.kotlin)
}

inline fun <reified VM : ViewModel> ComponentActivity.injectableViewModel(): Lazy<VM> =
    ViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { viewModelStore },
        factoryProducer = { ViewModelFactory },
    )
