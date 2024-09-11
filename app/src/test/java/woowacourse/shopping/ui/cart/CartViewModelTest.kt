package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.Product

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {
    private lateinit var viewModel: CartViewModel
    private lateinit var cartRepository: InMemoryCartRepository

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        cartRepository = InMemoryCartRepository()
        viewModel = CartViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `카트에 담긴 모든 상품을 가져올 수 있다`() =
        runTest {
            // given
            addCartProducts(3)

            // when
            viewModel.getAllCartProducts()

            // then
            val cartProducts = viewModel.cartProducts.getOrAwaitValue()
            assertThat(cartProducts).isEqualTo(
                listOf(
                    Product(1, "Product1", 1000, "image1"),
                    Product(2, "Product2", 2000, "image2"),
                    Product(3, "Product3", 3000, "image3"),
                ),
            )
        }

    @Test
    fun `카트에 담긴 상품을 삭제할 수 있다`() =
        runTest {
            // given
            addCartProducts(3)

            // when
            viewModel.getAllCartProducts()
            viewModel.deleteCartProduct(1)
            viewModel.getAllCartProducts()

            // then
            val cartProducts = viewModel.cartProducts.getOrAwaitValue()
            val onCartProductDeleted = viewModel.onCartProductDeleted.getOrAwaitValue()

            assertThat(cartProducts).isEqualTo(
                listOf(
                    Product(1, "Product1", 1000, "image1"),
                    Product(3, "Product3", 3000, "image3"),
                ),
            )

            assertThat(onCartProductDeleted).isTrue()
        }

    private suspend fun addCartProducts(size: Int) {
        repeat(size) { index ->
            addCartProduct(index + 1)
        }
    }

    private suspend fun addCartProduct(index: Int) {
        cartRepository.addCartProduct(generateSingleProduct(index))
    }
}
