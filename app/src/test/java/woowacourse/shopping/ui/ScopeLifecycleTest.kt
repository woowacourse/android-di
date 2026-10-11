package woowacourse.shopping.ui

import android.app.Application
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.junit.After
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import woowacourse.shopping.R
import woowacourse.shopping.ScreenScope
import woowacourse.shopping.appScopeKey
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.annotation.RoomCart
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.di.DependencyKey
import woowacourse.shopping.di.ScopeKey
import woowacourse.shopping.ui.cart.DateFormatter

@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
class ScopeLifecycleTest {
    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var diContainer: DIContainer

    @Before
    fun setUp() {
        diContainer = DIContainer()
        diContainer.openScope(appScopeKey)
        diContainer.bindInstance(Context::class, RuntimeEnvironment.getApplication(), scopeKey = appScopeKey)
        diContainer.bindInstance(
            type = CartRepository::class,
            instance = InMemoryCartRepository(),
            qualifier = RoomCart::class,
            scopeKey = appScopeKey,
        )
        diContainer.bind(DateFormatter::class, DateFormatter::class, scopeType = ScreenScope)
    }

    @After
    fun tearDown() {
        diContainer.closeScope(appScopeKey)
    }

    @Test
    fun `장바구니 화면을 나가면 화면 스코프가 닫힌다`() {
        val application = RuntimeEnvironment.getApplication()
        lateinit var navController: NavHostController
        composeRule.setContent {
            navController = rememberNavController()
            ShoppingNavHost(diContainer = diContainer, navController = navController)
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
