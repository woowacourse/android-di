package woowacourse.shopping.ui

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.savedstate.SavedStateRegistryOwner
import woowacourse.shopping.di.DependencyInjector

class AutoViewModelFactory(
    owner: SavedStateRegistryOwner,
) : AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle,
    ): T {
        val kClass = modelClass.kotlin
        val instance = DependencyInjector.getInstance(kClass, handle)

        DependencyInjector.injectAnnotatedProperties(kClass, instance)
        return instance
    }
}

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.autoViewModel(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> Factory)? = {
        AutoViewModelFactory(this)
    },
): Lazy<VM> =
    ViewModelLazy(
        VM::class,
        { viewModelStore },
        factoryProducer ?: { defaultViewModelProviderFactory },
        { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras },
    )
