package woowacourse.shopping.data.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object ViewModelFactory {
    inline fun <reified T : ViewModel> provide(
    ): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                Injector.inject<T>()
            }
        }
    }
}