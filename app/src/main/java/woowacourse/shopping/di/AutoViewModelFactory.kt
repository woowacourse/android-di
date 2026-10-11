package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.di.DependencyScope
import woowacourse.di.ScopedDependencyResolver

class AutoViewModelFactory(
    private val dependencyResolver: ScopedDependencyResolver,
    private val createScope: () -> DependencyScope = {
        dependencyResolver.openScope(ShoppingScopes.VIEW_MODEL)
    },
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val scope = createScope()
        return try {
            val viewModel =
                modelClass.cast(dependencyResolver.createInScope(modelClass.kotlin, scope))
                    ?: error("ViewModel 생성 결과가 요청한 타입과 다릅니다: ${modelClass.name}")
            viewModel.addCloseable(SCOPE_CLOSEABLE_KEY, scope)
            viewModel
        } catch (exception: Throwable) {
            scope.close()
            throw exception
        }
    }

    private companion object {
        const val SCOPE_CLOSEABLE_KEY = "woowacourse.shopping.di.viewModelScope"
    }
}
