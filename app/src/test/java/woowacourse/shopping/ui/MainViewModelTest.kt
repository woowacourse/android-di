package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.fake.FakeProductRepository
import woowacourse.shopping.fixture.CHICKEN_BREAST
import woowacourse.shopping.fixture.EGG
import woowacourse.shopping.fixture.POTATO
import woowacourse.shopping.ui.utils.getOrAwaitValue

@RunWith(RobolectricTestRunner::class)
class MainViewModelTest {
    @get:Rule
    val instant = InstantTaskExecutorRule()
    private lateinit var viewModel: MainViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository

    @Before
    fun setup() {
        productRepository = FakeProductRepository()
        cartRepository = FakeCartRepository()
        viewModel = MainViewModel(productRepository, cartRepository)
    }

    @Test
    fun `카트가 추가되면 onProductAdded가 true가 되는지 테스트`() {
        // when
        viewModel.addCartProduct(POTATO)
        // then
        assertTrue(viewModel.onProductAdded.getOrAwaitValue())
    }

    @Test
    fun `모든 제품을 가져오는지 테스트`() {
        // given
        val expected = listOf(POTATO, CHICKEN_BREAST, EGG)
        // when
        viewModel.getAllProducts()
        // then
        assertThat(viewModel.products.getOrAwaitValue()).isEqualTo(expected)
    }
}
