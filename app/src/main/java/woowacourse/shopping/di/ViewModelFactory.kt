package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

inline fun <reified T : ViewModel> getViewModelFactory(): ViewModelProvider.Factory =
    viewModelFactory {
        initializer {
            Injector.inject<T>()
        }
    }
