package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.fixture.ProductFixture
import woowacourse.shopping.repository.CartRepository

class CartViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var vm: CartViewModel
    private lateinit var cartRepository: CartRepository

    @Before
    fun setup() {
        cartRepository = mockk()
        vm = CartViewModel(cartRepository)
    }

    @Test
    fun `장바구니에 담긴 모든 상품을 요청하면 불러와진다`() {
        val fakeProducts = ProductFixture.getProducts(listOf(1, 2, 3, 4, 5))

        // given
        every { cartRepository.getAllCartProducts() } returns fakeProducts

        // when
        vm.getAllCartProducts()

        // then
        Assertions.assertThat(vm.cartProducts.value).isEqualTo(fakeProducts)
    }

    @Test
    fun `상품 삭제 요청하면 상품이 삭제된다`() {
        // given
        every { cartRepository.deleteCartProduct(any()) } just runs

        // when
        vm.deleteCartProduct(1)

        // then
        Assertions.assertThat(vm.onCartProductDeleted.value).isTrue
    }
}
