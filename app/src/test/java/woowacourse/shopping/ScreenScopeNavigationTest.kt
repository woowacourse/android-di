package woowacourse.shopping

import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cksckckcks.di.ScopedContainer
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.shopping.di.ShoppingScopes
import woowacourse.shopping.ui.ShoppingNavHost
import woowacourse.shopping.ui.ShoppingRoute
import woowacourse.shopping.ui.cart.DateFormatter

@RunWith(RobolectricTestRunner::class)
class ScreenScopeNavigationTest {
    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var navController: NavHostController
    private val revision = mutableIntStateOf(0)
    private val container get() = (RuntimeEnvironment.getApplication() as ShoppingApplication).container.diContainer

    @Test
    fun `재구성과 다른 화면에 가려져도 같은 포맷터를 유지하고 pop하면 정리한다`() {
        launch()
        navigate(ShoppingRoute.CART)
        val scope = screenScopes().single()
        val formatter = scope.getInstance(DateFormatter::class)
        val instances = scope.readPrivateField("instances") as Map<*, *>

        composeRule.runOnIdle { revision.intValue++ }
        composeRule.waitForIdle()
        assertThat(screenScopes().single().getInstance(DateFormatter::class)).isSameInstanceAs(formatter)

        navigate(ShoppingRoute.PRODUCTS)
        assertThat(screenScopes().single().getInstance(DateFormatter::class)).isSameInstanceAs(formatter)
        pop()
        assertThat(screenScopes().single().getInstance(DateFormatter::class)).isSameInstanceAs(formatter)
        pop()

        assertThat(screenScopes()).isEmpty()
        assertThat(instances).isEmpty()
        assertThat(scope.readPrivateField("container")).isNull()
    }

    @Test
    fun `같은 경로의 두 엔트리는 서로 다른 화면 스코프를 가진다`() {
        launch()
        navigate(ShoppingRoute.CART)
        val firstScope = screenScopes().single()
        val firstFormatter = firstScope.getInstance(DateFormatter::class)
        navigate(ShoppingRoute.CART)
        val secondScope = screenScopes().single { it !== firstScope }

        assertThat(secondScope.id).isNotEqualTo(firstScope.id)
        assertThat(secondScope.getInstance(DateFormatter::class)).isNotSameInstanceAs(firstFormatter)
        pop()
        assertThat(screenScopes().single()).isSameInstanceAs(firstScope)
        assertThat(firstScope.getInstance(DateFormatter::class)).isSameInstanceAs(firstFormatter)
        assertThat(secondScope.readPrivateField("container")).isNull()
        pop()
        assertThat(screenScopes()).isEmpty()
    }

    @Test
    fun `반복해서 재진입하면 새 포맷터가 생기고 스코프가 누적되지 않는다`() {
        launch()
        var previous: Any? = null
        repeat(3) {
            navigate(ShoppingRoute.CART)
            val scope = screenScopes().single()
            val formatter = scope.getInstance(DateFormatter::class)
            assertThat(formatter).isNotSameInstanceAs(previous)
            previous = formatter
            pop()
            assertThat(screenScopes()).isEmpty()
        }
    }

    private fun launch() {
        composeRule.setContent {
            val controller = rememberNavController()
            SideEffect {
                navController = controller
            }
            key(revision.intValue) { ShoppingNavHost(controller) }
        }
        composeRule.waitForIdle()
    }

    private fun navigate(route: String) {
        composeRule.runOnIdle { navController.navigate(route) }
        composeRule.waitForIdle()
    }

    private fun pop() {
        composeRule.runOnIdle { check(navController.popBackStack()) }
        composeRule.waitForIdle()
    }

    private fun screenScopes(): List<ScopedContainer> =
        (container.readPrivateField("scopes") as Map<*, *>)
            .values
            .filterIsInstance<ScopedContainer>()
            .filter { it.type == ShoppingScopes.screen }

    private fun Any.readPrivateField(name: String): Any? = javaClass.getDeclaredField(name).apply { isAccessible = true }.get(this)
}
