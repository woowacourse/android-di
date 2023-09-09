package woowacourse.shopping.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.dummy.FakeCartRepository
import woowacourse.shopping.dummy.FakeProductRepository
import woowacourse.shopping.dummy.products
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import woowacourse.shopping.ui.MainViewModel

class MainViewModelTest {

    private lateinit var cartRepository: CartRepository
    private lateinit var productRepository: ProductRepository
    private lateinit var viewModel: MainViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        cartRepository = FakeCartRepository()
        productRepository = FakeProductRepository()
        viewModel = MainViewModel()
    }

    @Test
    fun `상품들을 불러온다`() {
        // given & when
        viewModel.getAllProducts()

        // then
        assertEquals(viewModel.products.value, products)
    }

    @Test
    fun `상품을 카트에 추가할 수 있다`() {
        // given
        val product = Product(
            name = "우테코 과자",
            price = 10_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
        )

        // when
        viewModel.addCartProduct(product)

        // then
        assertEquals(viewModel.onProductAdded.value, true)
    }
}
