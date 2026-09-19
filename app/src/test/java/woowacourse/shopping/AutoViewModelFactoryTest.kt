package woowacourse.shopping

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.di.AutoViewModelFactory
import woowacourse.shopping.di.DependencyContainer
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel

class AutoViewModelFactoryTest {
    @Test
    fun `ProductViewModel을 자동으로 생성한다`() {
        val viewModel =
            AutoViewModelFactory.create(ProductsViewModel::class.java)

        assertThat(viewModel).isInstanceOf(ProductsViewModel::class.java)
    }

    @Test
    fun `CartViewModel을 자동으로 생성한다`() {
        val viewModel =
            AutoViewModelFactory.create(CartViewModel::class.java)

        assertThat(viewModel).isInstanceOf(CartViewModel::class.java)
    }

    @Test
    fun `두 ViewModel이 같은 CartRepository를 공유한다`() {
        val productsViewModel =
            AutoViewModelFactory.create(ProductsViewModel::class.java)
        val cartViewModel =
            AutoViewModelFactory.create(CartViewModel::class.java)

        val product =
            Product(
                name = "우테코 과자",
                price = 10_000,
                imageUrl = "",
            )

        productsViewModel.addCartProduct(product)
        cartViewModel.getAllCartProducts()

        assertThat(cartViewModel.uiState.value.cartProducts)
            .containsExactly(product)

        assertThat(
            DependencyContainer.get(CartRepository::class),
        ).isSameAs(
            DependencyContainer.get(CartRepository::class),
        )
    }
}
