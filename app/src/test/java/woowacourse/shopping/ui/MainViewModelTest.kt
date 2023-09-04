package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.dummy.FakeCartRepository
import woowacourse.shopping.dummy.FakeProductRepository
import woowacourse.shopping.dummy.createFakeProduct
import woowacourse.shopping.dummy.createFakeProducts
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class MainViewModelTest {

    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository
    private lateinit var viewModel: MainViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        productRepository = FakeProductRepository()
        cartRepository = FakeCartRepository()

        viewModel = MainViewModel(productRepository, cartRepository)
    }

    @Test
    fun `상품 리스트를 가져온다`() {
        // when
        viewModel.getAllProducts()

        // then
        assert(viewModel.products.getOrAwaitValue().size == 10)
        assert(viewModel.products.getOrAwaitValue() == createFakeProducts())
    }

    @Test
    fun `상품을 장바구니에 추가한다`() {
        // given
        val product = createFakeProduct(3)

        // when
        viewModel.addCartProduct(product)

        // then
        assert(viewModel.onProductAdded.getOrAwaitValue() == true)
    }
}
