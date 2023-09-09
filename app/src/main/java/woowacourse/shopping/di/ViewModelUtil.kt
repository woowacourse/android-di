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
inline fun <reified VM : ViewModel> ComponentActivity.viewModel(): Lazy<VM> {
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        { viewModelFactory<VM>() },
    )
}

inline fun <reified VM : ViewModel> viewModelFactory(): ViewModelProvider.Factory =
    viewModelFactory {
        initializer {
            ShoppingApplication.injector.inject<VM>()
        }
    }
