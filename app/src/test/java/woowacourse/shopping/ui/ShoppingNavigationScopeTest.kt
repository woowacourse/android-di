package woowacourse.shopping.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.di.DependencyScope
import woowacourse.di.dependencyContainer
import woowacourse.shopping.di.ShoppingScopes
import woowacourse.shopping.ui.cart.DateFormatter

@RunWith(RobolectricTestRunner::class)
class ShoppingNavigationScopeTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `장바구니 화면 재진입은 새 DateFormatter를 만들고 이전 스코프를 정리한다`() {
        val scopes = mutableListOf<DependencyScope>()
        val formatters = mutableListOf<DateFormatter>()

        composeRule.setContent {
            val context = LocalContext.current
            val container =
                remember(context) {
                    dependencyContainer {
                        registerFactory(DateFormatter::class, scope = ShoppingScopes.NAVIGATION_DESTINATION) {
                            DateFormatter(context)
                        }
                    }
                }
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "products") {
                composable("products") {
                    Button(onClick = { navController.navigate("cart") }) {
                        Text("장바구니로 이동")
                    }
                }
                composable("cart") { backStackEntry ->
                    val scope = rememberNavigationDestinationScope(backStackEntry, container)
                    val dateFormatter =
                        remember(scope) {
                            container
                                .getInScope(DateFormatter::class, scope)
                                .also(formatters::add)
                        }
                    var revision by remember { mutableIntStateOf(0) }
                    SideEffect {
                        if (scopes.none { it === scope }) scopes += scope
                    }
                    CartDestinationContent(
                        dateText = dateFormatter.formatDate(0L),
                        revision = revision,
                        onRefresh = { revision += 1 },
                        onBack = navController::navigateUp,
                    )
                }
            }
        }

        repeat(3) { index ->
            composeRule.onNodeWithText("장바구니로 이동").performClick()
            composeRule.waitForIdle()
            composeRule.onNodeWithText("화면 갱신 0").assertExists()
            assertThat(formatters).hasSize(index + 1)

            composeRule.onNodeWithText("화면 갱신 0").performClick()
            composeRule.waitForIdle()
            composeRule.onNodeWithText("화면 갱신 1").assertExists()
            assertThat(formatters).hasSize(index + 1)

            composeRule.onNodeWithText("뒤로가기").performClick()
            composeRule.waitForIdle()
            assertThat(scopes[index].isClosed).isTrue()
        }

        assertThat(scopes).hasSize(3)
        assertThat(scopes.map { it.id }.distinct()).hasSize(3)
        assertThat(formatters[0]).isNotSameAs(formatters[1])
        assertThat(formatters[1]).isNotSameAs(formatters[2])
        assertThat(scopes.all { it.isClosed }).isTrue()
    }
}

@Composable
private fun CartDestinationContent(
    dateText: String,
    revision: Int,
    onRefresh: () -> Unit,
    onBack: () -> Unit,
) {
    Column {
        Text("장바구니")
        Text(dateText)
        Button(onClick = onRefresh) { Text("화면 갱신 $revision") }
        Button(onClick = onBack) { Text("뒤로가기") }
    }
}
