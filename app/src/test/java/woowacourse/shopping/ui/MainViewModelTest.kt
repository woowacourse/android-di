package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.fixture.ProductFixture
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var vm: MainViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository

    @Before
    fun setup() {
        productRepository = mockk()
        cartRepository = mockk()
        vm = MainViewModel(productRepository, cartRepository)
    }

    @Test
    fun `모든 상품을 요청하면 모든 상품이 불러와진다`() {
        val fakeProducts = ProductFixture.getProducts(listOf(1, 2, 3, 4, 5))

        // given
        every { productRepository.getAllProducts() } returns fakeProducts

        // when
        vm.getAllProducts()

        // then
        assertThat(vm.products.value).isEqualTo(fakeProducts)
    }

    @Test
    fun `상품 추가 요청하면 상품이 담긴다`() {
        val fakeProduct = ProductFixture.getProduct()

        // given
        every { cartRepository.addCartProduct(any()) } just runs

        // when
        vm.addCartProduct(fakeProduct)

        // then
        assertThat(vm.onProductAdded.value).isTrue
    }
}
