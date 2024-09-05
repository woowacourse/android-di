package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.ProductFixture
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.FakeCartRepository
import woowacourse.shopping.getOrAwaitValue

class CartViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var cartRepository: CartRepository
    private lateinit var vm: CartViewModel

    @Before
    fun setUp() {
        cartRepository =
            FakeCartRepository(
                ProductFixture(1),
                ProductFixture(2),
            )
        vm = CartViewModel(cartRepository)
    }

    @Test
    fun `장바구니에 있는 상품 삭제 이벤트`() {
        // when
        vm.deleteCartProduct(1)

        // then
        assertThat(vm.onCartProductDeleted.getOrAwaitValue()).isEqualTo(true)
    }

    @Test
    fun `두 개의 상품이 있는 장바구니에 있는 하나의 상품 삭제`() {
        // when
        vm.deleteCartProduct(1)

        // then
        assertThat(vm.cartProducts.getOrAwaitValue()).contains(
            ProductFixture(1),
        )
    }
}
