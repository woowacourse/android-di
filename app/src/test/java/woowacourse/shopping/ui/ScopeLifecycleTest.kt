package woowacourse.shopping.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.shopping.R
import woowacourse.shopping.ScreenScope
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.di.DependencyKey
import woowacourse.shopping.di.ScopeKey
import woowacourse.shopping.ui.cart.DateFormatter

@RunWith(RobolectricTestRunner::class)
class ScopeLifecycleTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `장바구니 화면을 나가면 화면 스코프가 닫힌다`() {
        val application = RuntimeEnvironment.getApplication() as ShoppingApplication
        val diContainer = application.diContainer
        lateinit var navController: NavHostController
        composeRule.setContent {
            navController = rememberNavController()
            ShoppingNavHost(navController = navController)
        }

        composeRule.runOnIdle { navController.navigate(ShoppingRoute.CART) }
        composeRule.onNodeWithText(application.getString(R.string.cart_title)).assertIsDisplayed()

        lateinit var scopeKey: ScopeKey
        lateinit var formatter: DateFormatter
        composeRule.runOnIdle {
            val entry = requireNotNull(navController.currentBackStackEntry)
            scopeKey = ScopeKey(ScreenScope, entry.id)
            formatter = diContainer.createInstance(DependencyKey(DateFormatter::class), scopeKey)
            assertSame(formatter, diContainer.createInstance(DependencyKey(DateFormatter::class), scopeKey))
        }

        composeRule.runOnIdle { navController.navigateUp() }
        composeRule.waitForIdle()

        composeRule.runOnIdle {
            assertThrows(IllegalStateException::class.java) {
                diContainer.createInstance(DependencyKey(DateFormatter::class), scopeKey)
            }
        }
    }
}
