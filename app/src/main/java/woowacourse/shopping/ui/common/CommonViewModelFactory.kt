package woowacourse.shopping.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CommonViewModelFactory<T : ViewModel>(
    private val viewModelInitializer: () -> T,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(viewModelInitializer.invoke()::class.java)) {
            return viewModelInitializer.invoke() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
