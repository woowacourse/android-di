package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dygames.di.DependencyInjector.inject
import com.dygames.di.dependencies
import com.dygames.di.provider
import com.dygames.di.qualifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.CartProduct
import woowacourse.shopping.FakeCartRepository
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.Room
import woowacourse.shopping.model.DatabaseIdentifier

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `장바구니의 상품을 가져오면 Repository의 모든 상품을 반환한다`() {
        // given
        val expect = listOf(CartProduct())
        val cartRepository = FakeCartRepository(expect)

        dependencies {
            qualifier(Room()) {
                provider<CartRepository> { cartRepository }
            }
        }

        val viewModel = inject<CartViewModel>()

        // when
        viewModel.getAllCartProducts()

        // then
        val actual = viewModel.cartProducts.value
        assertEquals(expect, actual)
    }

    @Test
    fun `장바구니에 상품을 제거하면 상품 삭제 상태가 true가 되고 CartRepository에 상품을 제거한다`() {
        // given
        val products = listOf(
            CartProduct(identifier = DatabaseIdentifier(0)),
            CartProduct(identifier = DatabaseIdentifier(1))
        )
        val cartRepository = FakeCartRepository(products)

        dependencies {
            qualifier(Room()) {
                provider<CartRepository> { cartRepository }
            }
        }

        val viewModel = inject<CartViewModel>()

        // when
        viewModel.deleteCartProduct(products[0])

        // then
        val expect = listOf(CartProduct(identifier = DatabaseIdentifier(1)))
        assertEquals(true, viewModel.onCartProductDeleted.value)
        assertEquals(expect, cartRepository.products)
    }
}
