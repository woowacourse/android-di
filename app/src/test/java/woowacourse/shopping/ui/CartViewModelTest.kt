package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.MainDispatcherRule
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.ui.cart.CartViewModel

class CartViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instant = InstantTaskExecutorRule()

    @Test
    fun `장바구니_상품을_조회하면_순서대로_조회된다`() =
        runTest {
            // given
            val repository = mockk<CartRepository>()
            val cartProducts =
                listOf(
                    CartProduct(1L, "A", 10, "a", 111L),
                    CartProduct(2L, "B", 20, "b", 222L),
                )
            coEvery { repository.getAllCartProducts() } returns cartProducts
            val viewModel = CartViewModel(repository)

            // when
            viewModel.getAllCartProducts()

            // then
            assertThat(viewModel.cartProducts.getOrAwaitValue())
                .containsExactlyElementsIn(cartProducts)
                .inOrder()
            coVerify(exactly = 1) { repository.getAllCartProducts() }
        }

    @Test
    fun `장바구니_상품을_삭제하면_삭제_이벤트가_발생하고_해당_상품이_제거된다`() =
        runTest {
            // given
            val repository = mockk<CartRepository>(relaxed = true)
            coEvery { repository.getAllCartProducts() } returns emptyList()
            val viewModel = CartViewModel(repository)

            // when
            viewModel.deleteCartProduct(1L)

            // then
            assertThat(viewModel.onCartProductDeleted.getOrAwaitValue()).isTrue()
            coVerify(exactly = 1) { repository.deleteCartProduct(1L) }
            coVerify(exactly = 1) { repository.getAllCartProducts() }
        }
}
