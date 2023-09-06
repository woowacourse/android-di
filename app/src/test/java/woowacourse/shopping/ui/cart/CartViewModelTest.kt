package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.model.Product

class CartViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var vm: CartViewModel
    private lateinit var cartRepository: CartRepository
    private val fakeProduct = Product("name", 1000, "imageUrl")

    @Before
    fun setUp() {
        cartRepository = mockk()
        vm = CartViewModel(cartRepository = cartRepository)
    }

    @Test
    fun `장바구니에 담긴 모든 상품을 가져온다`() {
        // given
        val products = listOf(fakeProduct)
        every { cartRepository.getAllCartProducts() } returns products

        // when
        vm.getAllCartProducts()

        // then
        assertEquals(products, vm.cartProducts.value)
    }

    @Test
    fun `장바구니에 담긴 상품을 삭제한다`() {
        // given
        every { cartRepository.deleteCartProduct(any()) } just Runs

        // when
        vm.deleteCartProduct(0)

        // then
        assertEquals(true, vm.onCartProductDeleted.value)
    }
}
