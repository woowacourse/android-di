package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.di.DependencyScope
import woowacourse.di.ScopeType
import kotlin.reflect.full.cast

internal class ReflectionViewModelFactory(
    private val parent: DependencyScope,
    private val viewModelScopeType: ScopeType,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val scope = parent.openChild(viewModelScopeType)
        try {
            val viewModel = modelClass.kotlin.cast(scope.create(modelClass.kotlin))
            scope.inject(viewModel)
            viewModel.addCloseable(scope)
            return viewModel
        } catch (error: Throwable) {
            scope.close()
            throw error
        }
    }
}
