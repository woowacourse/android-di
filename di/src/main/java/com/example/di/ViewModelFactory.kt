package com.example.di

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.di.annotation.InjectedViewModel
import com.example.di.annotation.lifecycle.ViewModelLifeCycle
import kotlin.reflect.full.findAnnotation

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val kClass = modelClass.kotlin
        return if (ViewModel::class.java.isAssignableFrom(modelClass)) {
            val resultViewModel = Injector.inject(kClass) as T
            resultViewModel
                .apply {
                    addCloseable {
                        Injector.deleteFields(this, ViewModelLifeCycle::class)
                    }
                }
        } else {
            throw IllegalArgumentException("${modelClass}은 ViewModel 클래스가 아닙니다.")
        }
    }
}

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.injectViewModel(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null,
): Lazy<VM> {
    val factoryPromise =
        factoryProducer ?: {
            if (VM::class.findAnnotation<InjectedViewModel>() != null) {
                ViewModelFactory()
            } else {
                defaultViewModelProviderFactory
            }
        }

    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        factoryPromise,
        { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras },
    )
}
