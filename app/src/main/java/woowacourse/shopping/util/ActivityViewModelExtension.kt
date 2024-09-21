package woowacourse.shopping.util

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.ShoppingApplication

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.injectedViewModels(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = {
        viewModelFactory {
            initializer {
                (application as ShoppingApplication).injector.inject(VM::class)
            }
        }
    },
): Lazy<VM> {
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        factoryProducer ?: { defaultViewModelProviderFactory },
        { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras },
    )
}
