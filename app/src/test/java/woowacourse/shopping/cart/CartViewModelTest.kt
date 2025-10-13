package woowacourse.shopping.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.CoroutinesTestExtension
import woowacourse.extensions.getOrAwaitValue
import woowacourse.fake.FakeContainer
import woowacourse.fixture.CART_PRODUCTS_ENTITIES_FIXTURE
import woowacourse.shopping.Container
import woowacourse.shopping.data.db.toDomain
import woowacourse.shopping.domain.model.CartProduct
import woowacourse.shopping.ui.cart.vm.CartViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutinesTestExtension::class)
@RunWith(RobolectricTestRunner::class)
class CartViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var appContainer: Container
    private lateinit var viewModel: CartViewModel

    @Before
    fun setup() {
        appContainer = FakeContainer()
        viewModel = appContainer.viewModelFactory.create(CartViewModel::class.java)
    }

    @Test
    fun `장바구니에 담긴 모든 상품을 가져온다`() =
        runTest {
            // given
            val cartEntities = CART_PRODUCTS_ENTITIES_FIXTURE.map { it.toDomain() }

            // when
            viewModel.getAllCartProducts()
            advanceUntilIdle()

            // then
            val cartProduct: List<CartProduct> = viewModel.cartProducts.getOrAwaitValue()
            assertEquals(cartProduct, cartEntities)
            assertThat(cartProduct).hasSize(2)
        }

    @Test
    fun `장바구니에서 상품을 삭제한다`() =
        runTest {
            // given
            val deleteCardProductIndex = 0
            val cartEntities = CART_PRODUCTS_ENTITIES_FIXTURE.map { it.toDomain() }

            viewModel.getAllCartProducts()

            val cartProduct: List<CartProduct> = viewModel.cartProducts.getOrAwaitValue()
            assertEquals(cartProduct, cartEntities)

            // when
            viewModel.deleteCartProduct(deleteCardProductIndex)

            // then
            assertTrue(viewModel.onCartProductDeleted.getOrAwaitValue())
        }
}
