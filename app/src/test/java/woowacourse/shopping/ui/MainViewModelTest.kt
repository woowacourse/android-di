package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class MainViewModelTest {

    private lateinit var vm: MainViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        productRepository = mockk()
        cartRepository = mockk()
        vm = MainViewModel(productRepository, cartRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `상품을 추가하면 추가됨 상태가 true다`() {
        // given
        val fakeProduct = mockk<Product>()

        coEvery {
            cartRepository.addCartProduct(any())
        } just Runs

        // when
        vm.addCartProduct(fakeProduct)

        // then
        assertThat(vm.onProductAdded.getOrAwaitValue()).isTrue
    }

    @Test
    fun `전체 상품 목록 조회`() {
        // given
        val fakeProductList = listOf(
            Product(
                name = "Lester Foreman",
                price = 5957,
                imageUrl = "http://www.bing.com/search?q=postulant",
            ),
        )

        every {
            productRepository.getAllProducts()
        } answers {
            fakeProductList
        }

        // when
        vm.getAllProducts()

        // then
        assertThat(vm.products.getOrAwaitValue()).isEqualTo(fakeProductList)
    }
}
