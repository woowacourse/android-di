package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dygames.di.DependencyInjector.inject
import com.dygames.di.dependencies
import com.dygames.di.provider
import com.dygames.di.qualifier
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
import woowacourse.shopping.FakeCartRepository
import woowacourse.shopping.FakeProductRepository
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.Room
import woowacourse.shopping.model.Product

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val products = listOf(Product("", 0, ""))

    private val cartRepository = FakeCartRepository()
    private val productRepository = FakeProductRepository(products)

    @Before
    fun setUp() {
        dependencies {
            qualifier(Room()) {
                provider<CartRepository> { cartRepository }
            }
            provider<ProductRepository> { productRepository }
        }

        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `장바구니에 상품을 등록하면 상품 등록 상태가 true가 되고 CartRepository에 Product가 추가된다`() {
        // given
        val viewModel = inject<MainViewModel>()

        // when
        val expect = Product("", 0, "")
        viewModel.addCartProduct(expect)

        // then
        assertEquals(true, viewModel.onProductAdded.value)
        assertEquals(expect, cartRepository.products[0].product)
    }

    @Test
    fun `장바구니의 상품을 가져오면 CartRepository의 모든 상품을 가져온다`() {
        // given
        val viewModel = inject<MainViewModel>()

        // when
        viewModel.getAllProducts()

        // then
        val expect = products
        val actual = viewModel.products.value
        assertEquals(expect, actual)
    }
}
