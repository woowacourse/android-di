package woowacourse.shopping.di.viewmodel

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.boogiwoogi.di.version2.InstanceContainer
import woowacourse.shopping.ShoppingApplication

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.woogiViewModels(
    container: InstanceContainer = ShoppingApplication.container
): Lazy<VM> {
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        { woogiViewModelFactory<VM>(container) },
    )
}

inline fun <reified VM : ViewModel> woogiViewModelFactory(container: InstanceContainer): ViewModelProvider.Factory =
    viewModelFactory {
        initializer {
            ShoppingApplication.run {
                injector.inject<VM>(modules, container)
            }
        }
    }
