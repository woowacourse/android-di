package woowacourse.shopping.ui

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.runner.lifecycle.ActivityLifecycleCallback
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.ScreenScopeViewModel

@RunWith(RobolectricTestRunner::class)
class ScopeLeakTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: NavHostController

    @Before
    fun setUp() {
        composeRule.runOnUiThread {
            composeRule.activity.setContent {
                navController = rememberNavController()
                ShoppingNavHost(navController)
            }
        }
        composeRule.waitForIdle()
    }

    @Test
    fun `장바구니 화면을 나가면 화면 스코프가 한 번 정리된다`() {
        navigate(ShoppingRoute.CART)
        val cleared = watchCurrentScope()

        navigateUp()

        assertThat(cleared.count).isEqualTo(1)
    }

    @Test
    fun `장바구니 화면에 머무는 동안에는 화면 스코프가 정리되지 않는다`() {
        navigate(ShoppingRoute.CART)
        val cleared = watchCurrentScope()

        composeRule.waitForIdle()

        assertThat(cleared.count).isEqualTo(0)
    }

    @Test
    fun `화면이 회전되어도 화면 스코프는 정리되지 않는다`() {
        navigate(ShoppingRoute.CART)
        val cleared = watchCurrentScope()

        recreateActivity()

        assertThat(cleared.count).isEqualTo(0)
    }

    @Test
    fun `장바구니에 다시 들어오면 이전 화면 스코프만 정리된다`() {
        navigate(ShoppingRoute.CART)
        val first = watchCurrentScope()
        navigateUp()

        navigate(ShoppingRoute.CART)
        val second = watchCurrentScope()

        assertThat(first.count).isEqualTo(1)
        assertThat(second.count).isEqualTo(0)
    }

    private class ClearCounter : AutoCloseable {
        var count = 0
            private set

        override fun close() {
            count++
        }
    }

    private fun watchCurrentScope(): ClearCounter {
        val counter = ClearCounter()
        ViewModelProvider(navController.currentBackStackEntry!!)[ScreenScopeViewModel::class.java]
            .addCloseable(counter)
        return counter
    }

    private fun recreateActivity() {
        val setContentOnCreate =
            ActivityLifecycleCallback { activity, stage ->
                if (stage == Stage.CREATED) {
                    (activity as ComponentActivity).setContent {
                        navController = rememberNavController()
                        ShoppingNavHost(navController)
                    }
                }
            }
        val monitor = ActivityLifecycleMonitorRegistry.getInstance()
        monitor.addLifecycleCallback(setContentOnCreate)
        composeRule.activityRule.scenario.recreate()
        monitor.removeLifecycleCallback(setContentOnCreate)
        composeRule.waitForIdle()
    }

    private fun navigate(route: String) {
        composeRule.runOnUiThread { navController.navigate(route) }
        composeRule.waitForIdle()
    }

    private fun navigateUp() {
        composeRule.runOnUiThread { navController.navigateUp() }
        composeRule.waitForIdle()
    }
}
