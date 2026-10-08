package woowacourse.shopping.di

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.v2.createEmptyComposeRule
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.di.Injector
import woowacourse.di.get
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.ui.ShoppingNavHost
import woowacourse.shopping.ui.ShoppingRoute
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.products.ProductsViewModel
import woowacourse.shopping.ui.theme.ShoppingTheme

@RunWith(RobolectricTestRunner::class)
class ScreenScopeLifecycleTest {
    @get:Rule
    val composeRule = createEmptyComposeRule()

    @Test
    fun `장바구니 반복 진입은 포매터를 새로 만들고 이탈은 스코프를 닫는다`() {
        val controller = Robolectric.buildActivity(NavigationActivity::class.java).setup()
        composeRule.waitForIdle()
        val activity = controller.get()
        val root = (activity.application as ShoppingApplication).injector
        val repository = root.get<CartRepository>(RoomCart::class)
        var previous: DateFormatter? = null
        try {
            repeat(5) {
                composeRule.runOnIdle { activity.navController.navigate(ShoppingRoute.CART) }
                composeRule.waitForIdle()
                val scope = composeRule.runOnIdle { activity.screenScope() }
                val formatter = scope.get<DateFormatter>()
                assertThat(scope.get<DateFormatter>()).isSameAs(formatter)
                assertThat(formatter).isNotSameAs(previous)
                assertThat(scope.get<CartRepository>(RoomCart::class)).isSameAs(repository)
                previous = formatter

                composeRule.runOnIdle { assertThat(activity.navController.popBackStack()).isTrue() }
                composeRule.waitForIdle()

                assertThat(scope.isClosed).isTrue()
                assertThatThrownBy { scope.get<DateFormatter>() }.hasMessageContaining("이미 닫힌 스코프")
                assertThat(root.get<CartRepository>(RoomCart::class)).isSameAs(repository)
            }
        } finally {
            controller.pause().stop().destroy()
            root.close()
        }
    }

    @Test
    fun `구성 변경은 목적지 포매터와 ViewModel 저장소를 유지하고 최종 종료는 닫는다`() {
        val controller = Robolectric.buildActivity(NavigationActivity::class.java).setup()
        composeRule.waitForIdle()
        val initialActivity = controller.get()
        val root = (initialActivity.application as ShoppingApplication).injector
        val products =
            composeRule.runOnIdle {
                ViewModelProvider(initialActivity.navController.getBackStackEntry(ShoppingRoute.PRODUCTS))[
                    ProductsViewModel::class.java,
                ]
            }
        val modelScope = requireNotNull(products.getCloseable<Injector>(VIEW_MODEL_SCOPE_KEY))
        composeRule.runOnIdle { initialActivity.navController.navigate(ShoppingRoute.CART) }
        composeRule.waitForIdle()
        val scope = composeRule.runOnIdle { initialActivity.screenScope() }
        val formatter = scope.get<DateFormatter>()
        try {
            controller.recreate()
            composeRule.waitForIdle()
            val recreated = controller.get()
            composeRule.runOnIdle {
                assertThat(recreated).isNotSameAs(initialActivity)
                assertThat(recreated.navController.currentDestination?.route).isEqualTo(ShoppingRoute.CART)
                assertThat(recreated.screenScope()).isSameAs(scope)
                assertThat(recreated.screenScope().get<DateFormatter>()).isSameAs(formatter)
                val retainedProducts =
                    ViewModelProvider(recreated.navController.getBackStackEntry(ShoppingRoute.PRODUCTS))[
                        ProductsViewModel::class.java,
                    ]
                assertThat(retainedProducts).isSameAs(products)
                assertThat(retainedProducts.productRepository).isSameAs(products.productRepository)
                assertThat(scope.isClosed).isFalse()
                assertThat(modelScope.isClosed).isFalse()
            }
        } finally {
            controller.pause().stop().destroy()
        }
        assertThat(scope.isClosed).isTrue()
        assertThat(modelScope.isClosed).isTrue()
        assertThat(root.isClosed).isFalse()
        root.close()
    }

    class NavigationActivity : ComponentActivity() {
        lateinit var navController: NavHostController

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                navController = rememberNavController()
                ShoppingTheme { ShoppingNavHost(navController) }
            }
        }

        fun screenScope(): Injector =
            ViewModelProvider(requireNotNull(navController.currentBackStackEntry))[
                ScreenScopeViewModel::class.java,
            ].scope
    }
}
