package woowacourse.shopping.ui.cart

import woowacourse.shopping.data.FakeCartRepository
import woowacourse.shopping.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import woowacourse.shopping.InstantTaskExecutorExtension
import woowacourse.shopping.model.Product

@ExtendWith(InstantTaskExecutorExtension::class)
class CartViewModelTest {
    private lateinit var viewModel: CartViewModel
    private lateinit var cartRepository: FakeCartRepository

    @BeforeEach
    fun setUp() {
        cartRepository = FakeCartRepository()
        viewModel = CartViewModel(cartRepository)
    }

    @Test
    fun `카트에_담긴_모든_상품을_가져올_수_있다`() {
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
    fun `카트에_담긴_상품을_삭제할_수_있다`() {
        // given
        addCartProducts(3)

        // when
        viewModel.getAllCartProducts()
        viewModel.deleteCartProduct(1)
        viewModel.getAllCartProducts()

        // then
        val cartProducts = viewModel.cartProducts.getOrAwaitValue()
        val onCartProductDeleted = viewModel.onCartProductDeleted.getOrAwaitValue()

        assertAll(
            {
                assertThat(cartProducts).isEqualTo(
                    listOf(
                        Product("Product1", 1000, "image1"),
                        Product("Product3", 3000, "image3"),
                    )
                )
            },
            { assertThat(onCartProductDeleted).isTrue() },
        )
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
