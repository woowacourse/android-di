package woowacourse.shopping.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavBackStackEntry
import woowacourse.di.DependencyScope
import woowacourse.di.ScopedDependencyResolver
import woowacourse.shopping.di.ShoppingScopes
import androidx.lifecycle.viewmodel.compose.viewModel as composeViewModel

internal class NavigationDestinationScopeViewModel(
    val scope: DependencyScope,
) : ViewModel() {
    override fun onCleared() {
        scope.close()
    }
}

internal class NavigationDestinationScopeViewModelFactory(
    private val dependencyResolver: ScopedDependencyResolver,
    private val destinationId: String,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        check(modelClass.isAssignableFrom(NavigationDestinationScopeViewModel::class.java)) {
            "지원하지 않는 화면 스코프 ViewModel입니다: ${modelClass.name}"
        }
        val scope = dependencyResolver.openScope(ShoppingScopes.NAVIGATION_DESTINATION, destinationId)
        return try {
            modelClass.cast(NavigationDestinationScopeViewModel(scope))
                ?: error("화면 스코프 ViewModel을 생성할 수 없습니다: ${modelClass.name}")
        } catch (exception: Throwable) {
            scope.close()
            throw exception
        }
    }
}

@Composable
internal fun rememberNavigationDestinationScope(
    backStackEntry: NavBackStackEntry,
    dependencyResolver: ScopedDependencyResolver,
): DependencyScope {
    val factory =
        remember(backStackEntry.id, dependencyResolver) {
            NavigationDestinationScopeViewModelFactory(dependencyResolver, backStackEntry.id)
        }
    val scopeViewModel: NavigationDestinationScopeViewModel =
        composeViewModel(
            viewModelStoreOwner = backStackEntry,
            factory = factory,
        )
    return scopeViewModel.scope
}
