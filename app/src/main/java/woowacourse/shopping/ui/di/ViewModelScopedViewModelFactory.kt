package woowacourse.shopping.ui.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import io.github.firstwoosun.di.DependencyContainer
import io.github.firstwoosun.di.ScopeKind

class ViewModelScopedViewModelFactory(
    private val dependencyContainer: DependencyContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        createScopedViewModel(modelClass)

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T = createScopedViewModel(modelClass)

    private fun <T: ViewModel> createScopedViewModel(
        modelClass: Class<T>,
    ): T {
        val scope =
            dependencyContainer.applicationScope.openChild(
                ScopeKind.ViewModel
            )

        return try {
            val viewModel = modelClass.getDeclaredConstructor().newInstance()

            dependencyContainer.inject(
                target = viewModel,
                scope = scope,
            )

            viewModel.addCloseable(scope)

            viewModel
        } catch (error: Throwable) {
            scope.close()
            throw error
        }
    }
}
