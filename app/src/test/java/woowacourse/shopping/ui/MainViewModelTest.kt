package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.Room
import woowacourse.shopping.di.Dependencies
import woowacourse.shopping.di.DependencyInjector
import woowacourse.shopping.di.DependencyInjector.inject
import woowacourse.shopping.model.Product

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `장바구니에 상품을 등록하면 상품 등록 상태가 true가 되고 CartRepository에 Product가 추가된다`() {
        // given
        val cartRepository = FakeCartRepository()

        DependencyInjector.dependencies = object : Dependencies {
            @Room
            val cartRepository: CartRepository by lazy { cartRepository }

            @Room
            val productRepository: ProductRepository by lazy { DefaultProductRepository() }
        }

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
        val expect = listOf(Product("", 0, ""))
        val cartRepository = FakeCartRepository()
        val productRepository = FakeProductRepository(expect)

        DependencyInjector.dependencies = object : Dependencies {
            @Room
            val cartRepository: CartRepository by lazy { cartRepository }

            @Room
            val productRepository: ProductRepository by lazy { productRepository }
        }

        val viewModel = inject<MainViewModel>()

        // when
        viewModel.getAllProducts()

        // then
        val actual = viewModel.products.value
        assertEquals(expect, actual)
    }
}
