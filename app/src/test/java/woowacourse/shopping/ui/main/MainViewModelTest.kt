package woowacourse.shopping.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultFakeCartRepository
import woowacourse.shopping.data.DefaultFakeProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.Product

class MainViewModelTest {
    private lateinit var viewModel: MainViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupMainViewModel() {
        val productRepository: ProductRepository = DefaultFakeProductRepository()
        val cartRepository: CartRepository = DefaultFakeCartRepository()

        viewModel = MainViewModel(productRepository, cartRepository)
    }

    @Test
    fun `상품을 장바구니에 추가할 수 있다`() {
        // given
        val product: Product = ProductFixture.product

        // when
        viewModel.addCartProduct(product)

        // then
        viewModel.onProductAdded.getOrAwaitValue()

        val expectedIsAdded = true
        assertEquals(expectedIsAdded, viewModel.onProductAdded.value)
    }

    @Test
    fun `상품 정보를 불러온다`() {
        // given

        // when
        viewModel.getAllProducts()

        // then
        viewModel.products.getOrAwaitValue()

        val expectedProducts = ProductFixture.products
        assertEquals(expectedProducts, viewModel.products.value)
    }
}
