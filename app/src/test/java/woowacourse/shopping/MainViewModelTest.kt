package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.fake.FakeProductRepository
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.MainViewModel

class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `getAllProducts 호출 시 products LiveData가 업데이트된다`() {
        // given
        val productRepo = FakeProductRepository(ProductFixture.AllProducts)
        val cartRepo = FakeCartRepository()
        val viewModel =
            MainViewModel().apply {
                productRepository = productRepo
                cartRepository = cartRepo
            }

        // when
        viewModel.getAllProducts()

        // then
        assertEquals(ProductFixture.AllProducts, viewModel.products.getOrAwaitValue())
    }

    @Test
    fun `addCartProduct 호출 시 장바구니에 추가되고 onProductAdded가 true로 바뀐다`() {
        // given
        val productRepo = FakeProductRepository()
        val cartRepo = FakeCartRepository()
        val viewModel =
            MainViewModel().apply {
                productRepository = productRepo
                cartRepository = cartRepo
            }
        val item = Product("웨이퍼 초코바", 1000, "")

        // when
        viewModel.addCartProduct(item)

        // then
        assertTrue(viewModel.onProductAdded.getOrAwaitValue())
        assertEquals(listOf(item), cartRepo.getAllCartProducts())
    }
}
