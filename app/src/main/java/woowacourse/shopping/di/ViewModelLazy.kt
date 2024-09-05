package woowacourse.shopping.di

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.provideViewModel(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null,
): Lazy<VM> {
    val factoryPromise =
        factoryProducer ?: {
            ViewModelInjector()
        }

    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        factoryPromise,
        { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras },
    )
}
