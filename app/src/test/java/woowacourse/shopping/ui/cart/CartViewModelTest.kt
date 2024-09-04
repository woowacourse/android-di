package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import woowacourse.shopping.data.FakeCartRepository
import woowacourse.shopping.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.model.Product

class CartViewModelTest {
    private lateinit var viewModel: CartViewModel
    private lateinit var cartRepository: FakeCartRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        cartRepository = FakeCartRepository()
        viewModel = CartViewModel(cartRepository)
    }

    @Test
    fun `카트에 담긴 모든 상품을 가져올 수 있다`() {
        // given
        addCartProducts(3)

        // when
        viewModel.getAllCartProducts()

        // then
        val cartProducts = viewModel.cartProducts.getOrAwaitValue()
        assertThat(cartProducts).isEqualTo(
            listOf(
                Product("Product1", 1000, "image1"),
                Product("Product2", 2000, "image2"),
                Product("Product3", 3000, "image3"),
            )
        )
    }

    @Test
    fun `카트에 담긴 상품을 삭제할 수 있다`() {
        // given
        addCartProducts(3)

        // when
        viewModel.getAllCartProducts()
        viewModel.deleteCartProduct(1)
        viewModel.getAllCartProducts()

        // then
        val cartProducts = viewModel.cartProducts.getOrAwaitValue()
        val onCartProductDeleted = viewModel.onCartProductDeleted.getOrAwaitValue()

        assertThat(cartProducts).isEqualTo(
            listOf(
                Product("Product1", 1000, "image1"),
                Product("Product3", 3000, "image3"),
            )
        )

        assertThat(onCartProductDeleted).isTrue()
    }

    private fun addCartProducts(size: Int) {
        repeat(size) { index ->
            generateCartProduct(index + 1)
        }
    }

    private fun generateCartProduct(it: Int) {
        cartRepository.addCartProduct(
            Product("Product$it", it * 1000, "image$it")
        )
    }
}
