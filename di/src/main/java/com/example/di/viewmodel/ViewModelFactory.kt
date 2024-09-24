package com.example.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.di.DIContainer
import com.example.di.DIInjector
import com.example.di.DIModule
import com.example.di.annotation.LifeCycleScope
import kotlin.reflect.KClass

class ViewModelFactory(private val moduleType: KClass<*>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras,
    ): T {
        val module = DIContainer.getInstance(moduleType) as DIModule
        DIInjector.injectModule(module, LifeCycleScope.VIEW_MODEL)
        return DIInjector.createInstance(modelClass)
    }
}
