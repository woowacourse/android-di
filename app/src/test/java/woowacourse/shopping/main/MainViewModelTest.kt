package woowacourse.shopping.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.textfixture.FakeCartRepository
import woowacourse.shopping.textfixture.FakeProductRepository
import woowacourse.shopping.textfixture.TEST_PRODUCT
import woowacourse.shopping.textfixture.TEST_PRODUCTS
import woowacourse.shopping.ui.main.MainViewModel

class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(FakeProductRepository(TEST_PRODUCTS), FakeCartRepository())
    }

    @Test
    fun `장바구니에 상품을 추가할 수 있다`() {
        // given

        // when
        mainViewModel.addCartProduct(TEST_PRODUCT)

        // then
        Assert.assertTrue(mainViewModel.onProductAdded.getOrAwaitValue())
    }

    @Test
    fun `모든 상품을 조회할 수 있다`() {
        // given

        // when
        mainViewModel.getAllProducts()

        // then
        Assert.assertEquals(TEST_PRODUCTS, mainViewModel.products.getOrAwaitValue())
    }
}
