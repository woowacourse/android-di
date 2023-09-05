package woowacourse.shopping.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.Product

class MainViewModelTest {
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository
    private lateinit var viewModel: MainViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupMainViewModel() {
        productRepository = mockk()
        cartRepository = mockk()

        viewModel = MainViewModel(productRepository, cartRepository)
    }

    @Test
    fun `상품을 장바구니에 추가할 수 있다`() {
        // given
        val product = slot<Product>()
        every {
            cartRepository.addCartProduct(capture(product))
        } just runs

        // when
        viewModel.addCartProduct(ProductFixture.product)

        // then
        viewModel.onProductAdded.getOrAwaitValue()

        val expectedProduct = product.captured
        val expectedIsAdded = true
        assertEquals(expectedProduct, ProductFixture.product)
        assertEquals(expectedIsAdded, viewModel.onProductAdded.value)
    }

    @Test
    fun `상품 정보를 불러온다`() {
        // given
        every {
            productRepository.getAllProducts()
        } answers {
            ProductFixture.products
        }

        // when
        viewModel.getAllProducts()

        // then
        viewModel.products.getOrAwaitValue()

        val expectedProducts = ProductFixture.products
        assertEquals(expectedProducts, viewModel.products.value)
    }
}
