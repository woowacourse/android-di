package woowacourse.shopping.ui

import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContain
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import woowacourse.shopping.DEFAULT_PRODUCT
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

@ExtendWith(InstantTaskExecutorExtension::class)
class MainViewModelTest {
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository
    private lateinit var viewModel: MainViewModel

    @BeforeEach
    fun setUp() {
        productRepository = mockk<ProductRepository>(relaxed = true)
        cartRepository = mockk<CartRepository>(relaxed = true)
        viewModel = MainViewModel(productRepository, cartRepository)
    }

    @DisplayName("상품을 추가하면 상태가 변경된다")
    @Test
    fun addCartProductTest() {
        // given
        coEvery { cartRepository.addCartProduct(DEFAULT_PRODUCT) } just Runs

        // when
        viewModel.addCartProduct(DEFAULT_PRODUCT)

        // then
        viewModel.onProductAdded.getOrAwaitValue().shouldBeTrue()
        coVerify(exactly = 1) { cartRepository.addCartProduct(DEFAULT_PRODUCT) }
    }

    @DisplayName("상품 목록을 가져온다")
    @Test
    fun getAllProductsTest() {
        // given
        coEvery { productRepository.getAllProducts() } returns listOf(DEFAULT_PRODUCT)

        // when
        viewModel.getAllProducts()
        val products = viewModel.products.getOrAwaitValue()

        // then
        products.shouldContain(DEFAULT_PRODUCT)
        coVerify(exactly = 1) { productRepository.getAllProducts() }
    }
}
