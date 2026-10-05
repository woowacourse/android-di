package woowacourse.shopping.ui.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import io.github.firstwoosun.di.DependencyContainer
import io.github.firstwoosun.di.ScopeKind

class ScreenScopedViewModelFactory(
    private val dependencyContainer: DependencyContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == ScreenScopedViewModel::class.java)

        val scope =
            dependencyContainer.applicationScope.openChild(
                ScopeKind.Screen,
            )

        return try {
            modelClass.cast(ScreenScopedViewModel(scope))
        } catch (error: Throwable) {
            scope.close()
            throw error
        }
    }

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T = create(modelClass)
}
