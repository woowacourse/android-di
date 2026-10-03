package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.di.DIContainer
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.RoomCart
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel

class DIViewModelFactoryTest {
    private val factory =
        DIViewModelFactory(
            DIContainer().apply {
                register(CartRepository::class, FakeCartRepository(), RoomCart::class)
            },
        )

    @Test
    fun `여러 ViewModel을 동일한 팩토리로 생성한다`() {
        val productsViewModel = factory.create(ProductsViewModel::class.java)
        val cartViewModel = factory.create(CartViewModel::class.java)

        assertThat(productsViewModel).isInstanceOf(ProductsViewModel::class.java)
        assertThat(cartViewModel).isInstanceOf(CartViewModel::class.java)
    }

    private class FakeCartRepository : CartRepository {
        override suspend fun addCartProduct(product: Product) = Unit

        override suspend fun getAllCartProducts(): List<CartProduct> = emptyList()

        override suspend fun deleteCartProduct(id: Long) = Unit
    }
}
