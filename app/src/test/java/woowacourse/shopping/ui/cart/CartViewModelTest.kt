package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.createProduct
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.getProducts
import woowacourse.shopping.repository.CartRepository

class CartViewModelTest {
    private lateinit var cartRepository: CartRepository
    private lateinit var viewModel: CartViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        cartRepository = FakeCartRepository(getProducts().toMutableList())
        viewModel = CartViewModel(cartRepository)
    }

    @Test
    fun `카트 상품을 조회하면 카트에 담긴 모든 상품을 가져올 수 있다`() {
        // given

        // when
        viewModel.getAllCartProducts()

        // then
        val expected = getProducts()
        assertThat(viewModel.cartProducts.value).isEqualTo(expected)
    }

    @Test
    fun `카트 상품을 삭제하면 카트에서 상품이 삭제된다`() {
        // given
        val product = createProduct()
        viewModel.getAllCartProducts()
        assertThat(viewModel.cartProducts.value).contains(product)

        // when
        viewModel.deleteCartProduct(0)

        // then
        viewModel.getAllCartProducts()
        assertThat(viewModel.cartProducts.value).doesNotContain(product)
    }

    @Test
    fun `카트 상품을 삭제하면 카트 상품을 삭제했는지 여부가 참이 된다`() {
        // given
        assertThat(viewModel.onCartProductDeleted.value).isFalse

        // when
        viewModel.deleteCartProduct(0)

        // then
        assertThat(viewModel.onCartProductDeleted.value).isTrue
    }
}
