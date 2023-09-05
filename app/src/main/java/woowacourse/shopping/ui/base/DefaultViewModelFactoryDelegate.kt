package woowacourse.shopping.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.DiApplication

class DefaultViewModelFactoryDelegate : ViewModelFactoryDelegate {
    override val Factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return DiApplication.repositoryFactory.getTarget(modelClass.kotlin)
        }
    }
}
