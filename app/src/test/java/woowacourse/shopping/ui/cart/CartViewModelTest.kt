package woowacourse.shopping.ui.cart

import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContain
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import woowacourse.shopping.DEFAULT_PRODUCT
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.ui.InstantTaskExecutorExtension
import woowacourse.shopping.ui.getOrAwaitValue

@ExtendWith(InstantTaskExecutorExtension::class)
class CartViewModelTest {
    private lateinit var cartRepository: CartRepository
    private lateinit var viewModel: CartViewModel

    @BeforeEach
    fun setUp() {
        cartRepository = mockk<CartRepository>(relaxed = true)
        viewModel = CartViewModel(cartRepository)
    }

    @DisplayName("장바구니에 담긴 상품 목록을 가져온다")
    @Test
    fun getAllCartProductsTest() {
        // given
        coEvery { cartRepository.getAllCartProducts() } returns listOf(DEFAULT_PRODUCT)

        // when
        viewModel.getAllCartProducts()
        val products = viewModel.cartProducts.getOrAwaitValue()

        // then
        products shouldContain DEFAULT_PRODUCT
        coVerify(exactly = 1) { cartRepository.getAllCartProducts() }
    }

    @DisplayName("장바구니에 담긴 물건을 삭제하면 상태가 변경된다")
    @Test
    fun deleteCartProductTest() {
        // given
        val id = 1
        coEvery { cartRepository.deleteCartProduct(id) } just Runs

        // when
        viewModel.deleteCartProduct(id)
        val deleted = viewModel.onCartProductDeleted.getOrAwaitValue()

        // then
        deleted.shouldBeTrue()
        coVerify(exactly = 1) { cartRepository.deleteCartProduct(id) }
    }
}
