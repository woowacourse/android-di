package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.FakeCartRepository
import woowacourse.shopping.data.FakeProductRepository
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository
import woowacourse.shopping.di.DIContainer

class MainViewModelTest {
    private lateinit var viewModel: ViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        productRepository = FakeProductRepository()
        cartRepository = FakeCartRepository()
        DIContainer.setInstance(ProductRepository::class, productRepository)
        DIContainer.setInstance(CartRepository::class, cartRepository)
        viewModel = MainViewModel()
    }

    @Test
    fun `초기에 상품을 모두 불러온다`() {
        // when
        (viewModel as MainViewModel).getAllProducts()

        // then
        val products = productRepository.getAllProducts()
        assert((viewModel as MainViewModel).products.value == products)
    }

    @Test
    fun `카트에 상품 추가`() {
        // given
        val product = productRepository.getAllProducts().first()

        // when
        (viewModel as MainViewModel).addCartProduct(product)

        // then
        val cartProducts = cartRepository.getAllCartProducts()
        assert((viewModel as MainViewModel).onProductAdded.value == true)
        assert(cartProducts.size == 1)
    }

    @Test
    fun `dependencies are injected correctly`() {
        val productRepositoryField = MainViewModel::class.java.getDeclaredField("productRepository")
        productRepositoryField.isAccessible = true
        val injectedProductRepository = productRepositoryField.get(viewModel)
        assertNotNull(injectedProductRepository.toString(), "ProductRepository should be injected")
    }
}
