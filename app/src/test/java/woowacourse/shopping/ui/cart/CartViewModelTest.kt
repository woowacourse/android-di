package woowacourse.shopping.ui.cart

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.shopping.model.CartRepository
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

class CartViewModelTest {
    @Test
    fun `CartViewModel에 CartRepository가 정상적으로 주입된다`() {
        val viewModel = CartViewModel()

        val cartRepositoryProperty =
            viewModel::class
                .declaredMemberProperties
                .first { it.name == "cartRepository" }
                .apply {
                    this.isAccessible = true
                } as KProperty1<CartViewModel, CartRepository>

        val cartRepository: CartRepository = cartRepositoryProperty.get(viewModel)

        assertThat(cartRepository).isInstanceOf(CartRepository::class.java)
    }
}
