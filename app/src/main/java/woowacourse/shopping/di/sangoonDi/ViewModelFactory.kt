package woowacourse.shopping.di.sangoonDi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

inline fun <reified T : ViewModel> inject(): ViewModelProvider.Factory =
    viewModelFactory {
        initializer {
            DependencyInjector.inject<T>()
        }
    }
