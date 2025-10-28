package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.domain.Product
import woowacourse.shopping.domain.toData
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.getOrAwaitValue
import woowacourse.shopping.ui.model.toPresentation

@ExperimentalCoroutinesApi
class CartViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeCartRepository: FakeCartRepository
    private lateinit var viewModel: CartViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        fakeCartRepository = FakeCartRepository()
        viewModel = CartViewModel(fakeCartRepository)
    }

    @Test
    fun `장바구니 상품 조회 시 cartProducts가 업데이트된다`() =
        runTest {
            // Given
            val product1 = Product("상품1", 1000, "")
            val product2 = Product("상품2", 2000, "")
            val cartItem1 = product1.toData()
            val cartItem2 = product2.toData()

            fakeCartRepository.setCartProducts(listOf(cartItem1, cartItem2))

            // When
            viewModel.getAllCartProducts()
            val cartProducts = viewModel.cartProducts.getOrAwaitValue()

            // Then
            assertThat(cartProducts).isEqualTo(
                listOf(
                    cartItem1.toPresentation(),
                    cartItem2.toPresentation(),
                ),
            )
        }

    @Test
    fun `장바구니 상품 삭제 시 onCartProductDeleted가 true로 변한다`() =
        runTest {
            // Given
            val product1 = Product("상품1", 1000, "")
            val product2 = Product("상품2", 2000, "")
            fakeCartRepository.setCartProducts(listOf(product1.toData(), product2.toData()))

            // When
            viewModel.deleteCartProduct(0)
            val onDeleted = viewModel.onCartProductDeleted.getOrAwaitValue()

            // Then
            assertThat(onDeleted).isTrue()
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
