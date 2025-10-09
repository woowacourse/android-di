package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.domain.Product
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.getOrAwaitValue

class CartViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeCartRepository: FakeCartRepository
    private lateinit var viewModel: CartViewModel

    @Before
    fun setUp() {
        fakeCartRepository = FakeCartRepository()
        viewModel = CartViewModel(cartRepository = fakeCartRepository)
    }

    @Test
    fun `장바구니 상품 조회 시 cartProducts가 업데이트된다`() {
        // Given
        val product1 = Product("상품1", 1000, "")
        val product2 = Product("상품2", 2000, "")
        fakeCartRepository.setCartProducts(listOf(product1, product2))

        // When
        viewModel.getAllCartProducts()
        val cartProducts = viewModel.cartProducts.getOrAwaitValue()

        // Then
        assertThat(cartProducts).isEqualTo(listOf(product1, product2))
    }

    @Test
    fun `장바구니 상품 삭제 시 onCartProductDeleted가 true로 변한다`() {
        // Given
        val product1 = Product("상품1", 1000, "")
        val product2 = Product("상품2", 2000, "")
        fakeCartRepository.setCartProducts(listOf(product1, product2))

        // When
        viewModel.deleteCartProduct(0)
        val onDeleted = viewModel.onCartProductDeleted.getOrAwaitValue()

        // Then
        assertThat(onDeleted).isTrue()
    }
}
