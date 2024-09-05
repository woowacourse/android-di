package woowacourse.shopping.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.ui.ShoppingApplication.Companion.viewModelDependencyContainer

class ReflectiveViewModelFactory : ViewModelProvider.Factory {
    private val dependencyInjector =
        DependencyInjector(viewModelDependencyContainer)

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return dependencyInjector.createInstanceFromConstructor(modelClass)
    }
}
