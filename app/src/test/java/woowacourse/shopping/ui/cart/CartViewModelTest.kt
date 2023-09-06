package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.dummy.FakeCartRepository
import woowacourse.shopping.dummy.createFakeProducts
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class CartViewModelTest {

    private lateinit var cartRepository: CartRepository
    private lateinit var viewModel: CartViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        cartRepository = FakeCartRepository()

        viewModel = CartViewModel(cartRepository)
    }

    @Test
    fun `장바구니 상품들 가져온다`() {
        // when
        viewModel.getAllCartProducts()

        // then
        assert(viewModel.cartProducts.getOrAwaitValue().size == 10)
        assert(viewModel.cartProducts.getOrAwaitValue() == createFakeProducts())
    }

    @Test
    fun `장바구니 상품을 삭제한다`() {
        // given
        val product = Product(name = "Hey 3", price = 1000, imageUrl = "")

        // when
        viewModel.deleteCartProduct(3)

        // then
        assert(viewModel.onCartProductDeleted.getOrAwaitValue() == true)
        assert(!viewModel.cartProducts.getOrAwaitValue().contains(product))
    }
}
