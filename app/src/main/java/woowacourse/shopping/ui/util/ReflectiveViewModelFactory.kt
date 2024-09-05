package woowacourse.shopping.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class ReflectiveViewModelFactory : ViewModelProvider.Factory {
    private val dependencyInjector = DependencyInjector(ViewModelDependencyContainer)

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return dependencyInjector.createInstanceFromConstructor(modelClass)
    }
}
