package woowacourse.shopping.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.textfixture.TEST_PRODUCT
import woowacourse.shopping.textfixture.TEST_PRODUCTS
import woowacourse.shopping.ui.main.MainViewModel

class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository

    @Before
    fun setUp() {
        productRepository = mockk<ProductRepository>()
        cartRepository = mockk<CartRepository>()
        mainViewModel = MainViewModel(productRepository, cartRepository)
    }

    @Test
    fun `장바구니에 상품을 추가할 수 있다`() {
        // given
        every { cartRepository.addCartProduct(any()) } returns Unit

        // when
        mainViewModel.addCartProduct(TEST_PRODUCT)

        // then
        Assert.assertTrue(mainViewModel.onProductAdded.getOrAwaitValue())
        verify { cartRepository.addCartProduct(any()) }
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
