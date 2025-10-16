package woowacourse.shopping

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
import woowacourse.fixture.FakeCartRepository
import woowacourse.shopping.app.ui.cart.CartViewModel
import woowacourse.shopping.model.Product

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var cartRepository: FakeCartRepository
    private lateinit var viewModel: CartViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        cartRepository = FakeCartRepository()
        viewModel = CartViewModel()

        viewModel.cartRepository = cartRepository
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun product(
        id: Long,
        name: String,
    ) = Product(id, name, 10_000, "", 20011226)

    @Test
    fun `장바구니에 저장된 상품을 조회할 수 있다`() =
        runTest(testDispatcher) {
            cartRepository.apply {
                addCartProduct(product(0L, "A"))
                addCartProduct(product(1L, "B"))
            }

            viewModel.getAllCartProducts()

            val expected = cartRepository.getAllCartProducts()
            val actual = viewModel.cartProducts.getOrAwaitValue()
            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun `장바구니에 저장된 상품을 삭제할 수 있다`() =
        runTest(testDispatcher) {
            cartRepository.apply {
                addCartProduct(product(0L, "A"))
                addCartProduct(product(1L, "B"))
                addCartProduct(product(2L, "C"))
            }

            viewModel.deleteCartProduct(1L)
            viewModel.getAllCartProducts()

            val products = viewModel.cartProducts.getOrAwaitValue()
            assertThat(products.map { it.name }).containsExactly("A", "C")
            assertThat(viewModel.onCartProductDeleted.getOrAwaitValue()).isTrue()
        }
}
