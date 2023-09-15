package woowacourse.shopping.util

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.application.ShoppingApplication

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.injectViewModel(): Lazy<VM> {
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        ::viewModelFactory,
    )
}

fun viewModelFactory(): ViewModelProvider.Factory =
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ShoppingApplication.injector.inject(modelClass)
        }
    }
