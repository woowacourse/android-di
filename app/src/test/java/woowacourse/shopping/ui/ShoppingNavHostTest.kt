package woowacourse.shopping.ui

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.InMemoryCart
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.RoomCart
import woowacourse.shopping.data.RoomCartRepository
import woowacourse.shopping.di.DependencyContainer
import woowacourse.shopping.ui.cart.CartScreenScopeViewModel
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.theme.ShoppingTheme

@RunWith(RobolectricTestRunner::class)
class ShoppingNavHostTest {
    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var navController: NavHostController

    @Before
    fun setUp() {
        val cartProductDao =
            object : CartProductDao {
                override suspend fun getAll(): List<CartProductEntity> = emptyList()

                override suspend fun insert(cartProduct: CartProductEntity) = Unit

                override suspend fun delete(id: Long) = Unit
            }

        DependencyContainer.register(CartProductDao::class, cartProductDao)
        DependencyContainer.register(
            CartRepository::class,
            RoomCart::class,
            RoomCartRepository(cartProductDao),
        )
        DependencyContainer.register(
            CartRepository::class,
            InMemoryCart::class,
            InMemoryCartRepository(),
        )
    }

    @Test
    fun `Cart 목적지를 나갔다 재진입하면 ViewModel 의존성을 새로 만들고 앱 의존성은 공유한다`() {
        composeRule.setContent {
            navController = rememberNavController()
            ShoppingTheme {
                ShoppingNavHost(navController)
            }
        }
        composeRule.waitForIdle()

        composeRule.runOnIdle { navController.navigate(ShoppingRoute.CART) }
        composeRule.waitForIdle()
        val firstViewModel = cartViewModel()
        val firstScreenScope = cartScreenScopeViewModel()
        assertThat(cartScreenScopeViewModel()).isSameInstanceAs(firstScreenScope)

        composeRule.runOnIdle { navController.popBackStack() }
        composeRule.waitForIdle()
        composeRule.runOnIdle { navController.navigate(ShoppingRoute.CART) }
        composeRule.waitForIdle()
        val reenteredViewModel = cartViewModel()
        val reenteredScreenScope = cartScreenScopeViewModel()

        assertThat(reenteredScreenScope.dateFormatter)
            .isNotSameInstanceAs(firstScreenScope.dateFormatter)
        assertThat(reenteredViewModel.cartRepository)
            .isSameInstanceAs(firstViewModel.cartRepository)
    }

    private fun cartViewModel(): CartViewModel {
        lateinit var viewModel: CartViewModel
        composeRule.runOnIdle {
            viewModel =
                ViewModelProvider(
                    navController.getBackStackEntry(ShoppingRoute.CART),
                    DependencyContainer,
                ).get(CartViewModel::class.java)
        }
        return viewModel
    }

    private fun cartScreenScopeViewModel(): CartScreenScopeViewModel {
        lateinit var viewModel: CartScreenScopeViewModel
        composeRule.runOnIdle {
            viewModel =
                ViewModelProvider(
                    navController.getBackStackEntry(ShoppingRoute.CART),
                    DependencyContainer,
                ).get(CartScreenScopeViewModel::class.java)
        }
        return viewModel
    }
}
