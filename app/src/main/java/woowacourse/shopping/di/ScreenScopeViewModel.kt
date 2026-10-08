package woowacourse.shopping.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import woowacourse.di.Injector
import woowacourse.shopping.ShoppingApplication

internal class ScreenScopeViewModel(
    val scope: Injector,
) : ViewModel(scope) {
    class Factory(
        private val root: Injector,
        private val entryId: String,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras,
        ): T {
            require(modelClass == ScreenScopeViewModel::class.java) {
                "화면 스코프 소유자만 생성할 수 있습니다: ${modelClass.name}"
            }
            return modelClass.cast(ScreenScopeViewModel(root.openScope("screen:$entryId", ShoppingScopes.Screen)))!!
        }
    }
}

@Composable
internal fun rememberScreenScope(entry: NavBackStackEntry): Injector {
    val application = LocalContext.current.applicationContext as ShoppingApplication
    val factory =
        remember(application, entry.id) {
            ScreenScopeViewModel.Factory(application.injector, entry.id)
        }
    return viewModel<ScreenScopeViewModel>(viewModelStoreOwner = entry, factory = factory).scope
}
