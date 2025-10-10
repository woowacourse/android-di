package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.fake.CART_PRODUCTS
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.getOrAwaitValue

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var cartViewModel: CartViewModel

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        cartViewModel = CartViewModel(cartRepository = FakeCartRepository())
    }

    @Test
    fun `카트에 있는 모든 품목을 cartProducts로 업데이트 한다`() =
        runTest {
            // given:
            // when:
            cartViewModel.getAllCartProducts()

            advanceUntilIdle()

            // then:
            val products = cartViewModel.cartProducts.getOrAwaitValue()
            assertSoftly {
                assertThat(products.size).isEqualTo(2)
                assertThat(products).isEqualTo(CART_PRODUCTS)
            }
        }

    @Test
    fun `카트에 있는 물품 중 하나를 삭제하면 onCartProductDeleted 값이 true 가 된다`() =
        runTest {
            // given:
            // when:
            cartViewModel.deleteCartProduct(0)

            advanceUntilIdle()

            // then:
            val onCartProductDeleted = cartViewModel.onCartProductDeleted.getOrAwaitValue()
            assertThat(onCartProductDeleted).isEqualTo(true)
        }
}
