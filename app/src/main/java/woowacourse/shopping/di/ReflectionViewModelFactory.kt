package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.di.DependencyContainer
import woowacourse.di.Scope
import woowacourse.di.ScopeType
import java.util.UUID

class ReflectionViewModelFactory(
    private val container: DependencyContainer,
    private val appScope: Scope,
    private val viewModelScopeType: ScopeType,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val scope = container.openScope(viewModelScopeType, UUID.randomUUID().toString(), parent = appScope)
        return try {
            scope.create(modelClass.kotlin).also { viewModel ->
                scope.inject(viewModel)
                viewModel.addCloseable(SCOPE_KEY, scope)
            }
        } catch (exception: Exception) {
            scope.close()
            throw exception
        }
    }

    internal companion object {
        const val SCOPE_KEY = "di.viewModelScope"
    }
}
