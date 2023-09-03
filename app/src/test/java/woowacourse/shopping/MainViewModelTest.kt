package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import woowacourse.shopping.ui.main.MainViewModel

internal class MainViewModelTest {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var fakeProductRepository: ProductRepository
    private lateinit var fakeCartRepository: CartRepository
    private lateinit var products: List<Product>

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        products = listOf(
            Product("픽셀 폴드", 1500000, ""),
            Product("갤럭시 z폴드 5", 1800000, ""),
            Product("갤럭시 z플립 5", 1200000, ""),
        )
        fakeProductRepository = FakeProductRepository(products)
        fakeCartRepository = FakeCartRepository(emptyList())
        mainViewModel = MainViewModel(fakeProductRepository, fakeCartRepository)
    }

    @Test
    fun `모든 상품 받기를 호출하면 products에 모든 상품이 담긴다`() {
        // given
        val expected = products.size

        // when
        mainViewModel.getAllProducts()
        val actual = mainViewModel.products.value?.size

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun `상품을 장바구니에 추가하면 상품이 장바구니 레포지토리에 추가된다`() {
        // given
        val expected = fakeCartRepository.getAllCartProducts().size + 1

        // when
        mainViewModel.addCartProduct(Product("갤럭시 탭 S9", 1200000, ""))
        val actual = fakeCartRepository.getAllCartProducts().size

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun `상품을 바구니에 추가하면 onProductAdded가 false에서 true가 된다`() {
        // given
        val initialOnProductAdded = mainViewModel.onProductAdded.value

        // when
        mainViewModel.addCartProduct(Product("갤럭시 탭 S9", 1200000, ""))
        val actual = mainViewModel.onProductAdded.value

        // then
        assertEquals(initialOnProductAdded, false)
        assertEquals(actual, true)
    }
}
