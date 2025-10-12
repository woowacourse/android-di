package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.MainDispatcherRule
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.main.MainViewModel

private class FakeProductRepository : ProductRepository {
    private val data = listOf(Product("X", 1, "x"), Product("Y", 2, "y"))

    override fun getAllProducts(): List<Product> = data
}

class MainViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `상품_조회시_상품이_순서대로_조회된다`() =
        runTest {
            // given
            val productRepository: ProductRepository = FakeProductRepository()
            val cartRepository: CartRepository = mockk(relaxed = true)
            val viewModel =
                MainViewModel(
                    productRepository = productRepository,
                    cartRepository = cartRepository,
                )

            // when
            viewModel.getAllProducts()

            // then
            val result = viewModel.products.getOrAwaitValue()
            assertThat(result.map { it.name }).containsExactly("X", "Y").inOrder()
        }

    @Test
    fun `상품을_추가하면_추가_이벤트가_발생한다`() =
        runTest {
            // given
            val productRepository: ProductRepository = FakeProductRepository()
            val cartRepository: CartRepository = mockk(relaxed = true)
            val viewModel =
                MainViewModel(
                    productRepository = productRepository,
                    cartRepository = cartRepository,
                )
            val product = Product("Z", 3, "z")

            // when
            viewModel.addCartProduct(product)

            // then
            assertThat(viewModel.onProductAdded.getOrAwaitValue()).isTrue()
            coVerify(exactly = 1) { cartRepository.addCartProduct(product) }
        }
}
