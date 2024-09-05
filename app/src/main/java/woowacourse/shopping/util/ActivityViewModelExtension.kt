package woowacourse.shopping.util

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.di.ViewModelFactory
import woowacourse.shopping.di.ViewModelInjector

@MainThread
public inline fun <reified VM : ViewModel> ComponentActivity.injectedViewModels(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = {
        ViewModelFactory(
            ViewModelInjector(
                (application as ShoppingApplication).repositoryModule,
            ),
        )
    },
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: { defaultViewModelProviderFactory }

    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        factoryPromise,
        { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras },
    )
}
