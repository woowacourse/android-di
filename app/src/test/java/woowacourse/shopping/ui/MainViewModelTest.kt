package woowacourse.shopping.ui

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import woowacourse.shopping.InstantTaskExecutorExtension
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.FakeCartRepository
import woowacourse.shopping.data.FakeProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.Product

@ExtendWith(InstantTaskExecutorExtension::class)
class MainViewModelTest {
    private lateinit var viewModel: MainViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository

    @BeforeEach
    fun setUp() {
        productRepository = FakeProductRepository()
        cartRepository = FakeCartRepository()
        viewModel = MainViewModel(productRepository, cartRepository)
    }

    @Test
    fun `카트에 상품을 추가할 수 있다`() {
        // given
        val product = Product("Product1", 1000, "image1")

        // when
        viewModel.addCartProduct(product)

        // then
        val productAdded = viewModel.onProductAdded.getOrAwaitValue()
        assertThat(productAdded).isTrue()
    }

    @Test
    fun `모든 상품을 가져올 수 있다`() {
        // when
        viewModel.getAllProducts()

        // then
        val products = viewModel.products.getOrAwaitValue()
        assertThat(products).isEqualTo(
            listOf(
                Product("Product1", 1000, "image1"),
                Product("Product2", 2000, "image2"),
                Product("Product3", 3000, "image3"),
            )
        )
    }
}
