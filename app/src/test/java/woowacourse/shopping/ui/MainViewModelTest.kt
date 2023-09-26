package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository
import woowacourse.shopping.ui.cart.ProductFixture.Product
import woowacourse.shopping.ui.cart.ProductFixture.Products

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository
    private lateinit var mainViewModel: MainViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        productRepository = mockk()
        cartRepository = mockk()
        mainViewModel = MainViewModel(productRepository, cartRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `장바구니에 상품을 추가하면 onProductAdded 가 true로 바뀐다`() {
        // given
        val product = Product()

        coEvery {
            cartRepository.addCartProduct(product)
        } answers { nothing }

        // when
        mainViewModel.addCartProduct(product)

        // then
        val actual: Boolean = mainViewModel.onProductAdded.value ?: false
        assertTrue(actual)
    }

    @Test
    fun `상품 목록을 불러온다`() {
        // given
        val products = Products()

        every {
            productRepository.getAllProducts()
        } answers { products }

        // when
        mainViewModel.getAllProducts()

        // then
        val actual = mainViewModel.products.value ?: listOf()
        assertEquals(products, actual)
    }
}
