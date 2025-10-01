package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.domain.Product
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.FakeProductRepository
import woowacourse.shopping.getOrAwaitValue

class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() // LiveData가 동기적으로 동작

    private lateinit var fakeProductRepository: FakeProductRepository
    private lateinit var fakeCartRepository: FakeCartRepository
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        fakeProductRepository = FakeProductRepository()
        fakeCartRepository = FakeCartRepository()

        viewModel =
            MainViewModel(
                cartRepository = fakeCartRepository,
                productRepository = fakeProductRepository,
            )
    }

    @Test
    fun `상품 전체 목록 조회 시 products가 업데이트된다`() {
        // Given
        val testProducts =
            listOf(
                Product("테스트1", 1000, ""),
                Product("테스트2", 2000, ""),
            )
        fakeProductRepository.setProducts(testProducts)

        // When
        viewModel.getAllProducts()
        val products = viewModel.products.getOrAwaitValue()

        // Then
        assertThat(products).isEqualTo(testProducts)
        assertThat(products.size).isEqualTo(2)
    }

    @Test
    fun `장바구니 상품 추가 성공 시 onProductAdded가 업데이트된다`() {
        // Given
        val product = Product("테스트상품", 1000, "")

        // When
        viewModel.addCartProduct(product)
        val onAdded = viewModel.onProductAdded.getOrAwaitValue()

        // Then
        assertThat(fakeCartRepository.getAllCartProducts()).containsExactly(product)
        assertThat(onAdded).isTrue()
    }
}
