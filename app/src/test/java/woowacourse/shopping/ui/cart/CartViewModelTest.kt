package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.productsFixture
import woowacourse.shopping.model.repository.CartRepository

class CartViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var cartRepository: CartRepository
    private lateinit var viewModel: CartViewModel

    @Before
    fun setup() {
        cartRepository = FakeCartRepository()
        viewModel = CartViewModel(cartRepository)
    }

    @Test
    fun `장바구니에 담긴 전체 상품을 조회할 수 있다`() {
        // Given
        cartRepository.addCartProduct(productsFixture[0])
        cartRepository.addCartProduct(productsFixture[1])

        // When
        viewModel.getAllCartProducts()

        // Then
        assertThat(viewModel.cartProducts.value).isEqualTo(
            listOf(
                productsFixture[0],
                productsFixture[1],
            ),
        )
    }

    @Test
    fun `장바구니에 담긴 상품을 삭제할 수 있다`() {
        // Given
        cartRepository.addCartProduct(productsFixture[0])
        cartRepository.addCartProduct(productsFixture[1])
        viewModel.getAllCartProducts()

        // When
        viewModel.deleteCartProduct(0)
        viewModel.getAllCartProducts()

        // Then
        assertThat(viewModel.cartProducts.value).isEqualTo(listOf(productsFixture[1]))
    }
}
