package woowacourse.shopping.ui

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.v2.createEmptyComposeRule
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ActivityScenario
import androidx.test.runner.lifecycle.ActivityLifecycleCallback
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.ScreenScopeViewModel
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.products.ProductsViewModel

@RunWith(RobolectricTestRunner::class)
class ScopeLifecycleTest {
    @get:Rule
    val composeRule = createEmptyComposeRule()

    private lateinit var navController: NavHostController
    private lateinit var scenario: ActivityScenario<ComponentActivity>

    // 실제 Activity처럼 onCreate 시점에 콘텐츠를 붙여야 재생성 시 내비게이션 상태가 복원된다
    private val setContentOnCreate =
        ActivityLifecycleCallback { activity, stage ->
            if (stage == Stage.CREATED) {
                (activity as ComponentActivity).setContent {
                    navController = rememberNavController()
                    ShoppingNavHost(navController)
                }
            }
        }

    @Before
    fun setUp() {
        ActivityLifecycleMonitorRegistry.getInstance().addLifecycleCallback(setContentOnCreate)
        scenario = ActivityScenario.launch(ComponentActivity::class.java)
        composeRule.waitForIdle()
    }

    @After
    fun tearDown() {
        scenario.close()
        ActivityLifecycleMonitorRegistry.getInstance().removeLifecycleCallback(setContentOnCreate)
    }

    @Test
    fun `장바구니 화면을 나갔다 다시 들어오면 DateFormatter가 새로 만들어진다`() {
        navigate(ShoppingRoute.CART)
        val first = currentDateFormatter()

        navigateUp()
        navigate(ShoppingRoute.CART)
        val second = currentDateFormatter()

        assertThat(second).isNotSameInstanceAs(first)
    }

    @Test
    fun `장바구니 화면 안에서는 같은 DateFormatter 인스턴스가 재사용된다`() {
        navigate(ShoppingRoute.CART)
        val first = currentDateFormatter()

        composeRule.waitForIdle()
        val second = currentDateFormatter()

        assertThat(second).isSameInstanceAs(first)
    }

    @Test
    fun `화면이 회전되어도 장바구니 화면의 DateFormatter 인스턴스가 유지된다`() {
        navigate(ShoppingRoute.CART)
        val first = currentDateFormatter()

        scenario.recreate()
        composeRule.waitForIdle()
        val second = currentDateFormatter()

        assertThat(navController.currentDestination?.route).isEqualTo(ShoppingRoute.CART)
        assertThat(second).isSameInstanceAs(first)
    }

    @Test
    fun `CartRepository는 앱이 살아 있는 동안 같은 인스턴스다`() {
        val productsRepository = currentViewModel<ProductsViewModel>().field<Any>("cartRepository")

        navigate(ShoppingRoute.CART)
        val firstCartViewModel = currentViewModel<CartViewModel>()
        navigateUp()
        navigate(ShoppingRoute.CART)
        val secondCartViewModel = currentViewModel<CartViewModel>()

        assertThat(secondCartViewModel).isNotSameInstanceAs(firstCartViewModel)
        assertThat(firstCartViewModel.field<Any>("cartRepository")).isSameInstanceAs(productsRepository)
        assertThat(secondCartViewModel.field<Any>("cartRepository")).isSameInstanceAs(productsRepository)
    }

    @Test
    fun `ProductRepository는 ViewModel과 함께 소멸한다`() {
        val firstViewModel = currentViewModel<ProductsViewModel>()

        composeRule.runOnUiThread {
            navController.navigate(ShoppingRoute.PRODUCTS) {
                popUpTo(ShoppingRoute.PRODUCTS) { inclusive = true }
            }
        }
        composeRule.waitForIdle()
        val secondViewModel = currentViewModel<ProductsViewModel>()

        assertThat(secondViewModel).isNotSameInstanceAs(firstViewModel)
        assertThat(secondViewModel.field<Any>("productRepository"))
            .isNotSameInstanceAs(firstViewModel.field<Any>("productRepository"))
    }

    private fun navigate(route: String) {
        composeRule.runOnUiThread { navController.navigate(route) }
        composeRule.waitForIdle()
    }

    private fun navigateUp() {
        composeRule.runOnUiThread { navController.navigateUp() }
        composeRule.waitForIdle()
    }

    private inline fun <reified VM : ViewModel> currentViewModel(): VM =
        ViewModelProvider(navController.currentBackStackEntry!!)[VM::class.java]

    private fun currentDateFormatter(): DateFormatter =
        currentViewModel<ScreenScopeViewModel>().scope.resolveDependencies(DateFormatter::class)

    @Suppress("UNCHECKED_CAST")
    private fun <T> Any.field(name: String): T = javaClass.getDeclaredField(name).apply { isAccessible = true }.get(this) as T
}
