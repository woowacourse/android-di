package woowacourse.shopping.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.woosuk.scott_di.inject

object ViewModelFactoryUtil {
    inline fun <reified T : ViewModel> viewModelFactory(): ViewModelProvider.Factory =
        viewModelFactory {
            initializer {
                inject<T>()
            }
        }
}
