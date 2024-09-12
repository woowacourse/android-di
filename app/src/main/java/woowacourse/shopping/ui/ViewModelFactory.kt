package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.di.DependencyProvider
import woowacourse.shopping.di.inject
import kotlin.reflect.KClass

class ViewModelFactory(private val dependencyProvider: DependencyProvider) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras,
    ): T {
        return inject(modelClass, dependencyProvider)
    }
}
