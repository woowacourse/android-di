package woowacourse.shopping.ui.util

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.viewmodel.CreationExtras
import olive.di.ViewModelFactory
import woowacourse.shopping.ShoppingApplication

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.diViewModels(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> Factory)? = null
): Lazy<VM> {
    val diContainer = (applicationContext as ShoppingApplication).diContainer
    val factoryPromise = factoryProducer ?: { ViewModelFactory(diContainer) }

    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        factoryPromise,
        { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras }
    )
}
