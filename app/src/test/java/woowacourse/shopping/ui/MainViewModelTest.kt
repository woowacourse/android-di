package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.FakeCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.getOrAwaitValue

class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

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
                cartRepository,
            )
    }

    @Test
    fun `모든 상품 조회`() {
        // when
        vm.getAllProducts()

        // then
        assertThat(vm.products.getOrAwaitValue()).isEqualTo(productRepository.allProducts())
    }
}
