package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.created.customdi.Injector

inline fun <reified T : ViewModel> inject(): ViewModelProvider.Factory =
    viewModelFactory {
        initializer {
            Injector.inject<T>()
        }
    }
