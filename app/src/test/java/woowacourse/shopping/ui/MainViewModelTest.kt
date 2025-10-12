package woowacourse.shopping.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.getOrAwaitValue
import woowacourse.shopping.model.Product

private class FakeProductRepository : ProductRepository {
    private val data = listOf(Product("X", 1, "x"), Product("Y", 2, "y"))

    override fun getAllProducts(): List<Product> = data
}

private class FakeCartRepository : CartRepository {
    private val cart = mutableListOf<Product>()

    override fun addCartProduct(product: Product) {
        cart.add(product)
    }

    override fun getAllCartProducts(): List<Product> = cart.toList()

    override fun deleteCartProduct(id: Int) {
        cart.removeAt(id)
    }
}

class MainViewModelTest {
    @get:Rule
    val instant = InstantTaskExecutorRule()

    @Test
    fun `상품_조회시_상품이_순서대로_조회된다`() {
        // given
        val viewModel =
            MainViewModel(
                productRepository = FakeProductRepository(),
                cartRepository = FakeCartRepository(),
            )

        // when
        viewModel.getAllProducts()

        // then
        val result = viewModel.products.getOrAwaitValue()
        assertThat(result.map { it.name }).containsExactly("X", "Y").inOrder()
    }

    @Test
    fun `상품을_추가하면_추가_이벤트가_발생한다`() {
        // given
        val viewModel =
            MainViewModel(
                productRepository = FakeProductRepository(),
                cartRepository = FakeCartRepository(),
            )

        // when
        viewModel.addCartProduct(Product("Z", 3, "z"))

        // then
        assertThat(viewModel.onProductAdded.getOrAwaitValue()).isTrue()
    }
}
