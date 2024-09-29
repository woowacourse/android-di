package woowacourse.shopping.di

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

@MainThread
inline fun <reified VM : ViewModel> DependencyInjectedActivity.injectViewModels(
    noinline extrasProducer: (() -> CreationExtras)? = null,
): Lazy<VM> {
    val factoryPromise: Factory =
        viewModelFactory {
            initializer {
                dependencyInjector.createInstance(VM::class)
            }
        }

    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        { factoryPromise },
        { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras }
    )
}
