package woowacourse.shopping.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        productRepository = FakeProductRepository()
        cartRepository = FakeCartRepository()
        viewModel = MainViewModel(productRepository, cartRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
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
