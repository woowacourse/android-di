package woowacourse.shopping.di

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.ShoppingApplication

@MainThread
inline fun <reified VM : ViewModel> ViewModelStoreOwner.getViewModel(injector: Injector = ShoppingApplication.injector): Lazy<VM> {
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        {
            viewModelFactory {
                initializer {
                    injector.inject(VM::class) as ViewModel
                }
            }
        },
    )
}
