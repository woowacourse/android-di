package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.fake.CARTS
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.getOrAwaitValue

class CartViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var cartViewModel: CartViewModel

    @Before
    fun setUp() {
        cartViewModel = CartViewModel(cartRepository = FakeCartRepository())
    }

    @Test
    fun `카트에 있는 모든 품목을 cartProducts로 업데이트 한다`() {
        // given:
        // when:
        cartViewModel.getAllCartProducts()

        // then:
        val products = cartViewModel.cartProducts.getOrAwaitValue()
        assertSoftly {
            assertThat(products.size).isEqualTo(2)
            assertThat(products).isEqualTo(CARTS)
        }
    }

    @Test
    fun `카트에 있는 물품 중 하나를 삭제하면 onCartProductDeleted 값이 true 가 된다`() {
        // given:
        // when:
        cartViewModel.deleteCartProduct(0)

        // then:
        val onCartProductDeleted = cartViewModel.onCartProductDeleted.getOrAwaitValue()
        assertThat(onCartProductDeleted).isEqualTo(true)
    }
}
