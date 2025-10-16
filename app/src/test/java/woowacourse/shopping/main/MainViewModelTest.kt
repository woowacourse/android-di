package woowacourse.shopping.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.fixture.TEST_PRODUCT
import woowacourse.shopping.fixture.TEST_PRODUCTS
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.ui.main.MainViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        productRepository = mockk<ProductRepository>()
        cartRepository = mockk<CartRepository>()
        mainViewModel = MainViewModel()

        val productRepositoryField = MainViewModel::class.java.getDeclaredField("productRepository")
        val cartRepositoryField = MainViewModel::class.java.getDeclaredField("cartRepository")

        productRepositoryField.isAccessible = true
        cartRepositoryField.isAccessible = true

        productRepositoryField.set(mainViewModel, productRepository)
        cartRepositoryField.set(mainViewModel, cartRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `장바구니에 상품을 추가할 수 있다`() =
        runTest {
            // given
            coEvery { cartRepository.addCartProduct(any()) } returns Unit

            // when
            mainViewModel.addCartProduct(TEST_PRODUCT)
            advanceUntilIdle()

            // then
            Assert.assertTrue(mainViewModel.onProductAdded.getOrAwaitValue())
            coVerify { cartRepository.addCartProduct(any()) }
        }

    @Test
    fun `모든 상품을 조회할 수 있다`() {
        // given
        every { productRepository.getAllProducts() } returns TEST_PRODUCTS

        // when
        mainViewModel.getAllProducts()

        // then
        verify { productRepository.getAllProducts() }
        Assert.assertEquals(TEST_PRODUCTS, mainViewModel.products.getOrAwaitValue())
    }
}
