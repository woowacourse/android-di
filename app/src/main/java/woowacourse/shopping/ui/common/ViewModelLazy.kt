package woowacourse.shopping.ui.common

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.ViewModelInitializer
import woowacourse.di.Injector
import woowacourse.shopping.ShoppingApplication

@MainThread
inline fun <reified VM : ViewModel> ViewModelStoreOwner.getViewModel(injector: Injector = ShoppingApplication.injector): Lazy<VM> {
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        {
            ViewModelProvider.Factory.from(ViewModelInitializer(
                VM::class.java
            ) {
                injector.inject()
            })
        },
    )
}
