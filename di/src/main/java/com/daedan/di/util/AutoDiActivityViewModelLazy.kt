package com.daedan.di.util

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.daedan.di.DiApplication
import com.daedan.di.qualifier.Qualifier
import com.daedan.di.qualifier.TypeQualifier

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.autoViewModels(
    qualifier: Qualifier = TypeQualifier(VM::class),
    noinline extrasProducer: (() -> CreationExtras)? = null,
): Lazy<VM> {
    val factory =
        viewModelFactory {
            initializer {
                val store = (this[APPLICATION_KEY] as DiApplication).appContainerStore
                store.instantiate(qualifier) as VM
            }
        }
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        { factory },
        { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras },
    )
}
