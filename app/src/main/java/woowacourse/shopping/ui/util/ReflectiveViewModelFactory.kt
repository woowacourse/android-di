package woowacourse.shopping.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.ui.ShoppingApplication.Companion.viewModelDependencyContainer

class ReflectiveViewModelFactory(
    private val dependencyInjector: DependencyInjector = DependencyInjector(
        viewModelDependencyContainer
    ),
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return dependencyInjector.createInstanceFromConstructor(modelClass)
    }
}
