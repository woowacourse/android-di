package woowacourse.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GlobalViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DependencyInjector.getInstance(modelClass)
    }
}
