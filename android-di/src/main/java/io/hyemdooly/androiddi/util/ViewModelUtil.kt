package io.hyemdooly.androiddi.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.hyemdooly.androiddi.base.HyemdoolyActivity

inline fun <reified VM : ViewModel> HyemdoolyActivity.viewModels(): Lazy<VM> {
    return ViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { viewModelStore },
        factoryProducer = {
            viewModelFactory {
                initializer {
                    this@viewModels.createViewModel(VM::class)
                }
            }
        },
    )
}
