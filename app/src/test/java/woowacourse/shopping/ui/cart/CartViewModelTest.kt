package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.fixture.GOBCHANG
import woowacourse.shopping.fixture.MALATANG
import woowacourse.shopping.fixture.TONKATSU
import woowacourse.shopping.getOrAwaitValue

class CartViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var cartRepository: CartRepository
    private lateinit var viewModel: CartViewModel

    @Before
    fun setUp() {
        cartRepository = FakeCartRepository().apply {
            addCartProduct(TONKATSU)
            addCartProduct(MALATANG)
            addCartProduct(GOBCHANG)
        }
        viewModel = CartViewModel(cartRepository)
    }

    @Test
    fun `카트에_담긴_상품_목록을_불러온다`() {
        // when
        viewModel.getAllCartProducts()
        val cartProducts = viewModel.cartProducts.getOrAwaitValue()

        // then
        assertTrue(cartProducts.containsAll(listOf(TONKATSU, MALATANG, GOBCHANG)))
    }

    @Test
    fun `카트에_담긴_물건을_삭제한다`() {
        // when
        viewModel.deleteCartProduct(1)
        val isDeleted = viewModel.onCartProductDeleted.getOrAwaitValue()

        // then
        assertTrue(isDeleted)
    }
}
