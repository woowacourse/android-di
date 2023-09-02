package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.di.DependencyInjector.inject

object ViewModelDependencyInjector {
    inline fun <reified T : ViewModel> injectViewModel(): ViewModelProvider.Factory =
        viewModelFactory {
            initializer {
                inject<T>()
            }
        }
}
