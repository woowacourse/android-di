package woowacourse.shopping.di

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.injectViewModel(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory) = { ViewModelFactory() },
): Lazy<VM> {
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        factoryProducer,
        { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras },
    )
}
