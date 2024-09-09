package woowacourse.shopping.ui.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory

inline fun <reified T> getInjectedViewModelFactory(): ViewModelProvider.Factory where T : ViewModel, T : DIInjection {
    return viewModelFactory {
        addInitializer(T::class) {
            createInjectedInstance(T::class)
        }
    }
}
