package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class InjectorViewModelFactory(
    private val dependencyInjector: DependencyInjector,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return dependencyInjector.inject(modelClass.kotlin)
    }
}
