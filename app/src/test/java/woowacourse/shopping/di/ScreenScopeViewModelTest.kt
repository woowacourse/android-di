package woowacourse.shopping.di

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.di.Inject
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.ShoppingNavHost
import woowacourse.shopping.ui.ShoppingRoute
import woowacourse.shopping.ui.cart.DateFormatter

@RunWith(RobolectricTestRunner::class)
class ScreenScopeViewModelTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `ProductRepository는 ViewModel별로 유지되고 종료 후 새로 생성된다`() {
        AppDI.initialize(RuntimeEnvironment.getApplication())
        val store = ViewModelStore()
        val factory = AppDI.viewModelFactory()
        val provider = ViewModelProvider(store, factory)
        val first = provider.get("first", ProductRepositoryViewModel::class.java)
        val second = provider.get("second", ProductRepositoryViewModel::class.java)

        assertThat(first.firstRepository).isSameInstanceAs(first.secondRepository)
        assertThat(second.firstRepository).isNotSameInstanceAs(first.firstRepository)

        store.clear()
        val recreated = ViewModelProvider(store, factory).get("first", ProductRepositoryViewModel::class.java)
        assertThat(recreated.firstRepository).isNotSameInstanceAs(first.firstRepository)
        store.clear()
    }

    @Test
    fun `화면 스코프는 구성 변경에 유지되고 백스택 종료 시 닫힌다`() {
        AppDI.initialize(RuntimeEnvironment.getApplication())
        val store = ViewModelStore()
        val factory = ViewModelProvider.NewInstanceFactory()
        val first = ViewModelProvider(store, factory)[ScreenScopeViewModel::class.java]
        val formatter = first.scope.get(DateFormatter::class)
        val cartRepository = first.scope.get(CartRepository::class, RoomCart::class)

        val afterRecreation = ViewModelProvider(store, factory)[ScreenScopeViewModel::class.java]
        assertThat(afterRecreation).isSameInstanceAs(first)
        assertThat(afterRecreation.scope.get(DateFormatter::class)).isSameInstanceAs(formatter)

        store.clear()
        assertThrows(IllegalStateException::class.java) { first.scope.get(DateFormatter::class) }

        val next = ViewModelProvider(store, factory)[ScreenScopeViewModel::class.java]
        assertThat(next.scope.get(DateFormatter::class)).isNotSameInstanceAs(formatter)
        assertThat(next.scope.get(CartRepository::class, RoomCart::class)).isSameInstanceAs(cartRepository)
        store.clear()
    }

    @Test
    fun `장바구니 목적지가 백스택에서 빠지면 화면 스코프가 닫힌다`() {
        AppDI.initialize(RuntimeEnvironment.getApplication())
        lateinit var navController: NavHostController
        composeRule.setContent {
            navController = rememberNavController()
            ShoppingNavHost(navController)
        }
        composeRule.runOnIdle { navController.navigate(ShoppingRoute.CART) }
        composeRule.waitForIdle()
        val entry = navController.getBackStackEntry(ShoppingRoute.CART)
        val screen = ViewModelProvider(entry)[ScreenScopeViewModel::class.java]
        assertThat(screen.scope.get(DateFormatter::class)).isNotNull()

        composeRule.runOnIdle { navController.navigateUp() }
        composeRule.waitForIdle()
        assertThrows(IllegalStateException::class.java) { screen.scope.get(DateFormatter::class) }
    }

    private class ProductRepositoryViewModel : ViewModel() {
        @Inject
        lateinit var firstRepository: ProductRepository

        @Inject
        lateinit var secondRepository: ProductRepository
    }
}
