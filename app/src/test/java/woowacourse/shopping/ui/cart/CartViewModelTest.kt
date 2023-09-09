package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.dummy.FakeCartRepository
import woowacourse.shopping.dummy.createFakeCartProducts
import woowacourse.shopping.dummy.createFakeProducts
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository

class CartViewModelTest {

    private lateinit var cartRepository: CartRepository
    private lateinit var viewModel: CartViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        cartRepository = FakeCartRepository()
        viewModel = CartViewModel(cartRepository)
    }

    @Test
    fun `장바구니 상품들 가져온다`() {
        // when
        viewModel.getAllCartProducts()

        // then
        assert(viewModel.cartProducts.getOrAwaitValue().size == 10)
        assert(viewModel.cartProducts.getOrAwaitValue() == createFakeCartProducts())
    }

    @Test
    fun `장바구니 상품을 삭제한다`() {
        // given
        val product = Product(name = "Hey 3", price = 1000, imageUrl = "")

        // when
        viewModel.deleteCartProduct(3)
        val expected = CartProduct(product.name, product.price, product.imageUrl, 1)

        // then
        assert(viewModel.onCartProductDeleted.getOrAwaitValue() == true)
        assert(!viewModel.cartProducts.getOrAwaitValue().contains(expected))
    }
}
