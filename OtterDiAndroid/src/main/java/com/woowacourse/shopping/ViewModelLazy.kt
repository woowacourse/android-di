package com.woowacourse.shopping

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

@MainThread
inline fun <reified VM : ViewModel> ViewModelStoreOwner.viewModels(injector: AndroidInjector): Lazy<VM> {
    val viewModelFactory = viewModelFactory {
        initializer {
            injector.inject<VM>()
        }
    }
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        { viewModelFactory },
    )
}
