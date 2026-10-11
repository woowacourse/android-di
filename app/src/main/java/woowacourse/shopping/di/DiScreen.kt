package woowacourse.shopping.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.harodi.DiManager
import com.harodi.ScopeKey
import woowacourse.shopping.ui.cart.DateFormatter

@Composable
fun DiScreen(
    diManager: DiManager,
    parentKey: ScopeKey,
    screen: @Composable (DateFormatter) -> Unit,
) {
    val screenScopeKey =
        remember {
            ScopeKey(
                parentKey = parentKey,
                scopeKind = Scope.SCREEN,
            )
        }

    DisposableEffect(Unit) {
        onDispose {
            diManager.removeScope(screenScopeKey)
        }
    }

    val dateFormatter = diManager.resolve(DateFormatter::class.java, screenScopeKey)

    screen(dateFormatter)
}
