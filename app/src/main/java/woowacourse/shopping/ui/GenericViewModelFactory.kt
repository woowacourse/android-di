package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.DIContainer

class GenericViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DIContainer.resolve(modelClass)
    }
}
