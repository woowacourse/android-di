package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.util.getCartProduct
import woowacourse.util.getProduct
import woowacourse.util.getProducts

internal class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        productRepository = mockk()
        cartRepository = mockk()
        viewModel =
            MainViewModel(cartRepository).also { it.productRepository = productRepository }
    }

    @Test
    fun `상품을 추가하면, 장바구니에 추가된다 `() {
        // given
        val carts = mutableListOf<CartProduct>()
        val addCartProductSlot = slot<Product>()
        coEvery { cartRepository.addCartProduct(capture(addCartProductSlot)) } answers {
            val cartProduct = getCartProduct(0L, addCartProductSlot.captured.name)
            carts.add(cartProduct)
        }

        // when
        viewModel.addCartProduct(getProduct("사과"))

        // then
        assertThat(carts.size).isEqualTo(1)
        assertThat(carts.first()).isEqualTo(getCartProduct(0L, "사과"))
    }

    @Test
    fun `상품 목록을 조회할 수 있다`() {
        // given
        every { productRepository.getAllProducts() } returns getProducts(listOf("사과", "포도", "수박"))

        // when
        viewModel.getAllProducts()

        // then
        val actual = viewModel.products.getOrAwaitValue()
        val expected = getProducts(listOf("사과", "포도", "수박"))
        assertThat(actual).isEqualTo(expected)
    }
}
