package woowacourse.shopping.di

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.di.ScopedContainer
import woowacourse.shopping.ui.ShoppingNavHost
import woowacourse.shopping.ui.ShoppingRoute
import woowacourse.shopping.ui.cart.DateFormatter

@RunWith(RobolectricTestRunner::class)
class ScopeLifecycleTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `장바구니 화면을 반복해서 나가면 이전 화면 스코프가 모두 닫힌다`() {
        lateinit var navController: NavHostController
        composeRule.setContent {
            navController = rememberNavController()
            ShoppingNavHost(navController)
        }

        val closedScopes = mutableListOf<ScopedContainer>()
        repeat(3) {
            composeRule.runOnIdle {
                navController.navigate(ShoppingRoute.CART)
            }
            composeRule.waitForIdle()

            composeRule.runOnIdle {
                val backStackEntry = navController.getBackStackEntry(ShoppingRoute.CART)
                val screenScope =
                    AppContainer.appScopedContainer.openScope(
                        type = ScreenScope,
                        id = backStackEntry.id,
                    )
                screenScope.get(DateFormatter::class)
                closedScopes += screenScope
            }

            composeRule.runOnIdle {
                navController.popBackStack()
            }
            composeRule.waitForIdle()
        }

        closedScopes.forEach { closedScope ->
            assertThrows(IllegalArgumentException::class.java) {
                closedScope.get(DateFormatter::class)
            }
        }
    }
}
