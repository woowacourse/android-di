package woowacourse.shopping.di

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.ShoppingApplication

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.woogiViewModels(): Lazy<VM> {
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        { woogiViewModelFactory<VM>() },
    )
}

inline fun <reified VM : ViewModel> woogiViewModelFactory(): ViewModelProvider.Factory =
    viewModelFactory {
        initializer {
            ShoppingApplication.injector.inject<VM>()
        }
    }
