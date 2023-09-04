package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.ui.MainViewModel

class MainViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var productRepository: DefaultProductRepository
    private lateinit var cartRepository: DefaultCartRepository

    @Before
    fun setup() {
        productRepository = mockk(relaxed = true)
        cartRepository = mockk(relaxed = true)

        viewModel = MainViewModel(
            productRepository = productRepository,
            cartRepository = cartRepository
        )
    }

    @Test
    fun `장바구니에 상품을 추가한 경우 상품을 추가했는지에 대한 여부가 참이 된다`() {
        // given
        val product = ProductFixture.products.first()

        // when
        viewModel.addCartProduct(product)
        val actual = viewModel.onProductAdded.getOrAwaitValue()

        // then
        val expected = true

        assertEquals(expected, actual)
    }

    @Test
    fun `모든 상품 목록들을 받아온다`() {
        // given
        every {
            productRepository.getAllProducts()
        } returns ProductFixture.products

        // when
        viewModel.getAllProducts()
        val actual = viewModel.products.getOrAwaitValue()

        // then
        val expected = ProductFixture.products

        assertEquals(expected, actual)
    }
}
