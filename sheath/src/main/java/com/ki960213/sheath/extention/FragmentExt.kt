package com.ki960213.sheath.extention

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ki960213.sheath.SheathApplication
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

@MainThread
inline fun <reified VM : ViewModel> Fragment.activityViewModels(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null,
): Lazy<VM> {
    val viewModelFactory = viewModelFactory {
        initializer {
            val viewModelComponent =
                SheathApplication.sheathComponentContainer[VM::class.createType()]
            viewModelComponent.getNewInstance() as VM
        }
    }

    return createViewModelLazy(
        VM::class,
        { requireActivity().viewModelStore },
        { extrasProducer?.invoke() ?: requireActivity().defaultViewModelCreationExtras },
        factoryProducer ?: { viewModelFactory },
    )
}

@MainThread
fun <VM : ViewModel> Fragment.createViewModelLazy(
    viewModelClass: KClass<VM>,
    storeProducer: () -> ViewModelStore,
    extrasProducer: () -> CreationExtras = { defaultViewModelCreationExtras },
    factoryProducer: (() -> ViewModelProvider.Factory)? = null,
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }
    return ViewModelLazy(viewModelClass, storeProducer, factoryPromise, extrasProducer)
}

@MainThread
inline fun <reified VM : ViewModel> Fragment.viewModels(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null,
): Lazy<VM> {
    val viewModelFactory = viewModelFactory {
        initializer {
            val viewModelComponent =
                SheathApplication.sheathComponentContainer[VM::class.createType()]
            viewModelComponent.getNewInstance() as VM
        }
    }

    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        factoryProducer ?: { viewModelFactory },
        { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras },
    )
}
