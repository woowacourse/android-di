package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
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
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository
import woowacourse.shopping.model.Product

class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var vm: MainViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository
    private val fakeProduct = Product("name", 1000, "imageUrl")

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        productRepository = mockk()
        cartRepository = mockk()
        vm = MainViewModel(productRepository = productRepository, cartRepository = cartRepository)
    }

    @After
    @OptIn(ExperimentalCoroutinesApi::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `상품을 장바구니에 담았을 때 장바구니 추가 상태를 true로 변경한다`() {
        // given
        every { vm.addCartProduct(any()) } just Runs

        // when
        vm.addCartProduct(fakeProduct)

        // then
        assertEquals(true, vm.onProductAdded.value)
    }

    @Test
    fun `모든 상품을 받아온다`() {
        // given
        val products = listOf(fakeProduct)
        every { productRepository.getAllProducts() } returns products

        // when
        vm.getAllProducts()

        // then
        assertEquals(products, vm.products.value)
    }
}
