package woowacourse.shopping.ui

import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContain
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import woowacourse.shopping.domain.model.Product
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
        val product = Product(name = "name", price = 1000, imageUrl = "")
        coEvery { cartRepository.addCartProduct(product) } just Runs

        // when
        viewModel.addCartProduct(product)

        // then
        viewModel.onProductAdded.getOrAwaitValue().shouldBeTrue()
    }

    @DisplayName("상품 목록을 가져온다")
    @Test
    fun getAllProductsTest() {
        // given
        val product = Product(name = "name", price = 1000, imageUrl = "")
        coEvery { productRepository.getAllProducts() } returns listOf(product)

        // when
        viewModel.getAllProducts()
        val products = viewModel.products.getOrAwaitValue()

        // then
        products.shouldContain(product)
    }
}
