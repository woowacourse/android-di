package woowacourse.shopping.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.di.LifecycleAwareDependencyInjector
import woowacourse.shopping.ui.ShoppingApplication

class ReflectiveViewModelFactory(private val injector: LifecycleAwareDependencyInjector) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        injector.initDependencyContainer(ShoppingApplication.getApplication().applicationDependencyContainer)
        return injector.createInstance(modelClass.kotlin)
    }
}
