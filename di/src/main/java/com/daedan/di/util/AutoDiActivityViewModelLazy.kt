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
import com.daedan.di.scope.Scope
import com.daedan.di.scope.TypeScope
import com.daedan.di.scope.UniqueScope

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.autoViewModels(
    qualifier: Qualifier = TypeQualifier(VM::class),
    scope: Scope = TypeScope(VM::class),
    noinline extrasProducer: (() -> CreationExtras)? = null,
): Lazy<VM> {
    val uniqueScope =
        UniqueScope(
            scope,
        )
    val factory =
        viewModelFactory {
            initializer {
                val store = (this[APPLICATION_KEY] as DiApplication).appContainerStore
                store.createScope(uniqueScope)
                val viewModel = store.instantiate(qualifier, uniqueScope) as VM
                viewModel.addCloseable { store.closeScope(uniqueScope) }
                viewModel
            }
        }
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        { factory },
        { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras },
    )
}
