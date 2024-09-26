package com.example.fake

import androidx.lifecycle.ViewModel
import com.example.di.DIInjector
import com.example.di.annotation.LifeCycleScope
import kotlin.reflect.KClass

abstract class FakeDIViewModel(private val moduleType: KClass<*>) : ViewModel() {
    public override fun onCleared() {
        super.onCleared()
        DIInjector.releaseModule(moduleType, LifeCycleScope.VIEW_MODEL)
    }
}
