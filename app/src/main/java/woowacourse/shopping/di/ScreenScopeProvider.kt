package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavBackStackEntry
import woowacourse.di.DependencyContainer
import woowacourse.di.Scope
import woowacourse.di.ScopeType

internal class ScreenScopeProvider(
    private val container: DependencyContainer,
    private val appScope: Scope,
    private val screenScopeType: ScopeType,
) {
    fun get(entry: NavBackStackEntry): Scope {
        val factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val scope = container.openScope(screenScopeType, entry.id, parent = appScope)
                    return requireNotNull(modelClass.cast(ScreenScopeViewModel(scope)))
                }
            }
        return ViewModelProvider(entry, factory)[ScreenScopeViewModel::class.java].scope
    }

    private class ScreenScopeViewModel(
        val scope: Scope,
    ) : ViewModel() {
        init {
            addCloseable(scope)
        }
    }
}
