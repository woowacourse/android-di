package woowacourse.shopping.di

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.otterdi.Injector

@MainThread
inline fun <reified VM : ViewModel> ViewModelStoreOwner.viewModels(injector: Injector = ShoppingApplication.injector): Lazy<VM> {
    val viewModelFactory = viewModelFactory {
        initializer {
            injector.inject<VM>()
        }
    }
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        { viewModelFactory },
    )
}
