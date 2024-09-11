package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.ProductFixture
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.FakeCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.getOrAwaitValue

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private lateinit var vm: MainViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository

    @Before
    fun setUp() {
        productRepository = DefaultProductRepository()
        cartRepository = FakeCartRepository()
        vm =
            MainViewModel(
                productRepository,
            )
        vm.cartRepository = cartRepository
    }

    @Test
    fun `모든 상품 조회`() {
        // when
        vm.getAllProducts()

        // then
        assertThat(vm.products.getOrAwaitValue()).isEqualTo(productRepository.allProducts())
    }

    @Test
    fun `장바구니에 상품 추가 이벤트`() {
        // when
        vm.addCartProduct(ProductFixture(1))

        // then
        assertThat(vm.onProductAdded.getOrAwaitValue()).isTrue()
    }
}
