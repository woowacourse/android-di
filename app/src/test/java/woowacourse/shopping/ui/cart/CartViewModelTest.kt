package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.Product

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `장바구니 상품을 조회하면 목록에 반영된다`() = runTest {
        // given
        val cartViewModel =
            CartViewModel().apply {
                cartRepository = FakeCartRepository().apply {
                    cartProducts.add(Product(0, "과자", 1000, "", 0L))
                    cartProducts.add(Product(1, "음료수", 1000, "", 0L))
                }
            }

        // when
        cartViewModel.getAllCartProducts()
        advanceUntilIdle()

        // then
        val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts.isNotEmpty()).isEqualTo(true)
    }

    @Test
    fun `장바구니 상품을 삭제하면 목록에 반영된다`() = runTest {
        // given
        val cartViewModel =
            CartViewModel().apply {
                cartRepository = FakeCartRepository().apply {
                    cartProducts.add(Product(0, "과자", 1000, "", 0L))
                    cartProducts.add(Product(1, "음료수", 1000, "", 0L))
                }
            }

        // when
        cartViewModel.deleteCartProduct(0)
        cartViewModel.getAllCartProducts()
        advanceUntilIdle()

        // then
        val cartProducts = cartViewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts.size).isEqualTo(1)
    }
}
