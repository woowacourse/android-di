package woowacourse.shopping.ui.common

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import woowacourse.di.Injector
import woowacourse.shopping.ShoppingApplication

@MainThread
inline fun <reified VM : ViewModel> ViewModelStoreOwner.getViewModel(injector: Injector = ShoppingApplication.injector): Lazy<VM> {
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return injector.inject(modelClass.kotlin)
                }
            }
        },
    )
}
