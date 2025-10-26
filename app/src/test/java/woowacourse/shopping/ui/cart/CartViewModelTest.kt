package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.example.di.di.InjectorViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.getOrAwaitValue

@RunWith(RobolectricTestRunner::class)
@Config(application = ShoppingApplication::class)
@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {
    private val application = ApplicationProvider.getApplicationContext<ShoppingApplication>()
    private lateinit var cartViewModel: CartViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())

        cartViewModel =
            InjectorViewModelFactory(
                dependencyInjector = (application as ShoppingApplication).dependencyInjector,
                scopeHolder = this,
            ).create(CartViewModel::class.java)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `장바구니 상품을 조회하면 목록에 반영된다`() =
        runTest {
            // given & when
            cartViewModel.getAllCartProducts()
            advanceUntilIdle()

            // then
            val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
            assertThat(cartProducts.isNotEmpty()).isEqualTo(true)
        }

    @Test
    fun `장바구니 상품을 삭제하면 목록에 반영된다`() =
        runTest {
            // given & when
            cartViewModel.deleteCartProduct(0)
            cartViewModel.getAllCartProducts()
            advanceUntilIdle()

            // then
            val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
            assertThat(cartProducts.size).isEqualTo(1)
        }
}
