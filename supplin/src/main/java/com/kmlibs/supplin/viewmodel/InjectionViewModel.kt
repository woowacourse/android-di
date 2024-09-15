package com.kmlibs.supplin.viewmodel

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.kmlibs.supplin.Injector

inline fun <reified VM : ViewModel> ComponentActivity.injectionViewModel(): Lazy<VM> {
    return viewModels {
        ViewModelFactory(
            viewModelClass = VM::class,
            instanceContainer = Injector.instanceContainer,
        )
    }
}