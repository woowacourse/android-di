package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.products.ProductsViewModel

class ProductRepositoryScopeTest {
    private class FakeCartProductDao : CartProductDao {
        override suspend fun getAll(): List<CartProductEntity> = emptyList()

        override suspend fun insert(cartProduct: CartProductEntity) = Unit

        override suspend fun delete(id: Long) = Unit
    }

    @Test
    fun `ProductRepository는 같은 ViewModel 스코프에서 재사용하고 다른 ViewModel과 분리한다`() {
        val viewModel = AppDI.viewModelScopeType
        val container = AppDI.createContainer(FakeCartProductDao())
        container.bind(ProductsViewModel::class, viewModel)
        val appScope = container.openScope(AppDI.appScopeType, "application")
        val firstScope = container.openScope(viewModel, "vm-1", parent = appScope)
        val secondScope = container.openScope(viewModel, "vm-2", parent = appScope)

        val firstViewModel = firstScope.resolve(ProductsViewModel::class)
        val secondViewModel = secondScope.resolve(ProductsViewModel::class)

        assertThat(firstViewModel).isNotSameAs(secondViewModel)
        assertThat(firstScope.resolve(ProductsViewModel::class)).isSameAs(firstViewModel)
        assertThat(firstViewModel.productRepository)
            .isSameAs(firstScope.resolve(ProductRepository::class))
        assertThat(secondViewModel.productRepository)
            .isSameAs(secondScope.resolve(ProductRepository::class))
        assertThat(firstViewModel.productRepository).isNotSameAs(secondViewModel.productRepository)
        assertThat(firstViewModel.cartRepository).isSameAs(secondViewModel.cartRepository)
    }

    @Test
    fun `Factory가 만든 ViewModel은 상품 저장소를 분리하고 장바구니 저장소를 공유한다`() {
        val container = AppDI.createContainer(FakeCartProductDao())
        val appScope = container.openScope(AppDI.appScopeType, "application")
        val factory = ReflectionViewModelFactory(container, appScope, AppDI.viewModelScopeType)

        val firstViewModel = factory.create(ProductsViewModel::class.java)
        val secondViewModel = factory.create(ProductsViewModel::class.java)

        assertThat(firstViewModel).isNotSameAs(secondViewModel)
        assertThat(firstViewModel.productRepository).isNotSameAs(secondViewModel.productRepository)
        assertThat(firstViewModel.cartRepository).isSameAs(secondViewModel.cartRepository)
    }
}
