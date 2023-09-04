package woowacourse.shopping.di

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.ShoppingApplication

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.viewModels(): Lazy<VM> {
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        {
            viewModelFactory {
                initializer {
                    ShoppingApplication.injector.inject<VM>()
                }
            }
        },
    )
}
