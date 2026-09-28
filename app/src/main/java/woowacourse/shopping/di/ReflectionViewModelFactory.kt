package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.di.DependencyContainer
import kotlin.reflect.full.cast

internal class ReflectionViewModelFactory(
    private val container: DependencyContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = container.create(modelClass.kotlin)
        container.inject(viewModel)
        return modelClass.kotlin.cast(viewModel)
    }
}
