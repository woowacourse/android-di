package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.di.DependencyInjector
import com.example.di.ViewModelFactory
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.FakeRepositoryModule
import woowacourse.shopping.fixture.PRODUCT_1
import woowacourse.shopping.fixture.PRODUCT_2
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.getOrAwaitValue

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    lateinit var viewModel: CartViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        DependencyInjector.initialize(FakeRepositoryModule())
        viewModel = ViewModelFactory.create(CartViewModel::class.java)
        viewModel.getAllCartProducts()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `장바구니 데이터를 불러올 수 있다`() =
        runTest {
            val actual: List<Product> =
                viewModel.cartProducts.getOrAwaitValue().map(CartProduct::product)
            val expected: List<Product> = listOf(PRODUCT_1, PRODUCT_2)
            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun `장바구니에서 특정 ID를 가진 상품을 제거할 수 있다`() =
        runTest {
            // given
            val initialProducts: List<Product> =
                viewModel.cartProducts.getOrAwaitValue().map(CartProduct::product)
            assertThat(initialProducts).isEqualTo(listOf(PRODUCT_1, PRODUCT_2))

            // when
            viewModel.deleteCartProduct(0)
            viewModel.getAllCartProducts()
            advanceUntilIdle()

            // then
            val finalProducts: List<Product> =
                viewModel.cartProducts.getOrAwaitValue().map(CartProduct::product)
            val expected: List<Product> = listOf(PRODUCT_2)
            assertThat(finalProducts).isEqualTo(expected)
        }

    @Test
    fun `장바구니에 담긴 상품을 제거하면 상품 제거 이벤트가 발생한다`() =
        runTest {
            // when
            viewModel.deleteCartProduct(0)
            advanceUntilIdle()

            // then
            val actual: Boolean = viewModel.onCartProductDeleted.getOrAwaitValue()
            assertThat(actual).isTrue()
        }
}
