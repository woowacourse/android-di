package woowacourse.shopping.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.Dummy.product
import woowacourse.shopping.FakeCartRepository
import woowacourse.shopping.FakeProductRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository
import woowacourse.shopping.ui.MainViewModel

class MainViewModelTest {
    private lateinit var viewModel: MainViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        productRepository = FakeProductRepository()
        cartRepository = FakeCartRepository()
        viewModel = MainViewModel(productRepository, cartRepository)
    }

    @Test
    fun `상품을 추가 하면 value 값이 true 로 바뀐다`() {
        // when
        viewModel.addCartProduct(product)

        // then
        assertEquals(true, viewModel.onProductAdded.getOrAwaitValue())
    }

    @Test
    fun `상품을 불러오면 products에 상품의 전체 목록이 저장된다`() {
        // when
        viewModel.getAllProducts()

        // then
        assertEquals(listOf(product), viewModel.products.getOrAwaitValue())
    }
}
