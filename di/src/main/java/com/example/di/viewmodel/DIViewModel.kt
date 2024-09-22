package com.example.di.viewmodel

import androidx.lifecycle.ViewModel
import com.example.di.DIInjector
import com.example.di.annotation.LifeCycleScope
import kotlin.reflect.KClass

abstract class DIViewModel(private val moduleType: KClass<*>) : ViewModel() {
    override fun onCleared() {
        super.onCleared()
        println("DIViewModel.onCleared()")
        DIInjector.releaseModule(moduleType, LifeCycleScope.VIEW_MODEL)
    }
}
