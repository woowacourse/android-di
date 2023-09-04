package woowacourse.shopping.data.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.ui.MyApplication

object ViewModelFactory {
    inline fun <reified T : ViewModel> provide(
    ): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                MyApplication.injector.inject<T>()
            }
        }
    }
}