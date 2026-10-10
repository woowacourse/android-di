package woowacourse.shopping

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.shopping.ui.ShoppingNavHost
import woowacourse.shopping.ui.ShoppingRoute
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.cart.DateFormatter

@RunWith(RobolectricTestRunner::class)
class ScreenScopeNavigationTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `백스택에 남은 화면은 유지하고 제거된 화면만 정리한다`() {
        lateinit var navController: NavHostController
        composeRule.setContent {
            navController = rememberNavController()
            ShoppingNavHost(navController)
        }
        val container = (RuntimeEnvironment.getApplication() as MyApplication).appContainer
        composeRule.runOnIdle { navController.navigate(ShoppingRoute.CART) }
        composeRule.waitForIdle()
        lateinit var first: CartViewModel
        lateinit var formatter: Any
        composeRule.runOnIdle {
            first = ViewModelProvider(navController.currentBackStackEntry!!)[CartViewModel::class.java]
            formatter = container.di.resolve(DateFormatter::class, scope = first.screenScope)
            navController.navigate(ShoppingRoute.CART)
        }
        composeRule.waitForIdle()
        lateinit var second: CartViewModel
        composeRule.runOnIdle {
            second = ViewModelProvider(navController.currentBackStackEntry!!)[CartViewModel::class.java]
            assertThat(second).isNotSameInstanceAs(first)
            assertThat(container.di.resolve(DateFormatter::class, scope = second.screenScope)).isNotSameInstanceAs(formatter)
            assertThat(first.screenScope.isClosed).isFalse()
            navController.popBackStack()
        }
        composeRule.waitForIdle()
        composeRule.runOnIdle {
            assertThat(second.screenScope.isClosed).isTrue()
            assertThat(second.screenScope.instanceCount).isEqualTo(0)
            assertThat(first.screenScope.isClosed).isFalse()
            assertThat(container.di.resolve(DateFormatter::class, scope = first.screenScope)).isSameInstanceAs(formatter)
            navController.popBackStack()
        }
        composeRule.waitForIdle()
        composeRule.runOnIdle {
            assertThat(first.screenScope.isClosed).isTrue()
            assertThat(first.screenScope.instanceCount).isEqualTo(0)
        }
    }

    @Test
    fun `실제 장바구니 진입과 이탈을 반복해도 포맷터와 스코프가 누적되지 않는다`() {
        lateinit var navController: NavHostController
        composeRule.setContent {
            navController = rememberNavController()
            ShoppingNavHost(navController)
        }
        val container = (RuntimeEnvironment.getApplication() as MyApplication).appContainer
        composeRule.waitForIdle()
        val appInstanceCount = container.appScope.instanceCount
        var previous: DateFormatter? = null

        repeat(10) {
            composeRule.runOnIdle { navController.navigate(ShoppingRoute.CART) }
            composeRule.waitForIdle()
            lateinit var holder: CartViewModel
            composeRule.runOnIdle {
                holder = ViewModelProvider(navController.currentBackStackEntry!!)[CartViewModel::class.java]
                val formatter = container.di.resolve(DateFormatter::class, scope = holder.screenScope) as DateFormatter
                assertThat(formatter).isNotSameInstanceAs(previous)
                assertThat(container.di.resolve(DateFormatter::class, scope = holder.screenScope)).isSameInstanceAs(formatter)
                assertThat(holder.screenScope.instanceCount).isEqualTo(1)
                previous = formatter
                navController.popBackStack()
            }
            composeRule.waitForIdle()
            composeRule.runOnIdle {
                assertThat(holder.screenScope.isClosed).isTrue()
                assertThat(holder.screenScope.instanceCount).isEqualTo(0)
                assertThat(holder.diScope.isClosed).isTrue()
                assertThat(holder.diScope.instanceCount).isEqualTo(0)
                assertThat(container.appScope.instanceCount).isEqualTo(appInstanceCount)
            }
        }
    }
}
