package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.di.DIContainer
import woowacourse.di.DIViewModelFactory
import woowacourse.di.annotation.RoomDB
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.fixture.GOBCHANG_CART
import woowacourse.shopping.fixture.MALATANG_CART
import woowacourse.shopping.fixture.MainDispatcherRule
import woowacourse.shopping.fixture.TONKATSU_CART
import woowacourse.shopping.getOrAwaitValue

class CartViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var cartRepository: CartRepository
    private lateinit var viewModel: CartViewModel

    @Before
    fun setUp() {
        runTest {
            val factory = DIViewModelFactory()
            cartRepository = FakeCartRepository()
            DIContainer.register(CartRepository::class, RoomDB::class) { cartRepository }
            viewModel = factory.create(CartViewModel::class.java)
        }
    }

    @Test
    fun `카트에_담긴_상품_목록을_불러온다`() =
        runTest {
            // when
            viewModel.getAllCartProducts()
            val cartProducts = viewModel.cartProducts.getOrAwaitValue()

            // then
            assertTrue(cartProducts.containsAll(listOf(TONKATSU_CART, MALATANG_CART, GOBCHANG_CART)))
        }

    @Test
    fun `카트에_담긴_물건을_삭제한다`() =
        runTest {
            // when
            viewModel.deleteCartProduct(1)
            val isDeleted = viewModel.onCartProductDeleted.getOrAwaitValue()

            // then
            assertTrue(isDeleted)
        }
}
