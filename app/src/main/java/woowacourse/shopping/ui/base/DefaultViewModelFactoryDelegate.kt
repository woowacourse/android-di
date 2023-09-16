package woowacourse.shopping.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.Injector

class DefaultViewModelFactoryDelegate : ViewModelFactoryDelegate {
    override val Factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return Injector.inject(modelClass.kotlin)
        }
    }
}
