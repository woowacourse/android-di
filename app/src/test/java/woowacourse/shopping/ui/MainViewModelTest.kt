package woowacourse.shopping.ui

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

class MainViewModelTest {
    @Test
    fun `MainViewModel에 ProductRepository가 정상적으로 주입된다`() {
        val viewModel = MainViewModel()

        val productRepositoryProperty =
            viewModel::class
                .declaredMemberProperties
                .first { it.name == "productRepository" }
                .apply {
                    this.isAccessible = true
                } as KProperty1<MainViewModel, ProductRepository>

        val productRepository: ProductRepository = productRepositoryProperty.get(viewModel)

        assertThat(productRepository).isInstanceOf(ProductRepository::class.java)
    }

    @Test
    fun `MainViewModel에 CartRepository가 정상적으로 주입된다`() {
        val viewModel = MainViewModel()

        val cartRepositoryProperty =
            viewModel::class
                .declaredMemberProperties
                .first { it.name == "cartRepository" }
                .apply {
                    this.isAccessible = true
                } as KProperty1<MainViewModel, CartRepository>

        val cartRepository: CartRepository = cartRepositoryProperty.get(viewModel)

        assertThat(cartRepository).isInstanceOf(CartRepository::class.java)
    }
}
