package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.di.MyInject
import woowacourse.di.ScopeType
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository

class CartRepositoryScopeTest {
    class TestCartViewModel : ViewModel() {
        @MyInject
        @RoomCart
        lateinit var roomRepository: CartRepository

        @MyInject
        @InMemoryCart
        lateinit var inMemoryRepository: CartRepository
    }

    private class FakeCartProductDao : CartProductDao {
        override suspend fun getAll(): List<CartProductEntity> = emptyList()

        override suspend fun insert(cartProduct: CartProductEntity) = Unit

        override suspend fun delete(id: Long) = Unit
    }

    @Test
    fun `서로 다른 ViewModel에 같은 Qualifier의 앱 스코프 CartRepository를 공유한다`() {
        val viewModel = ScopeType("viewModel")
        val dao = FakeCartProductDao()
        val container = AppDI.createContainer(dao)
        container.bind(TestCartViewModel::class, viewModel)
        val appScope = container.openScope(AppDI.appScopeType, "application")
        val firstScope = container.openScope(viewModel, "vm-1", parent = appScope)
        val secondScope = container.openScope(viewModel, "vm-2", parent = appScope)

        val firstViewModel = firstScope.resolve(TestCartViewModel::class)
        val secondViewModel = secondScope.resolve(TestCartViewModel::class)

        assertThat(firstViewModel).isNotSameAs(secondViewModel)
        assertThat(firstViewModel.roomRepository).isSameAs(secondViewModel.roomRepository)
        assertThat(firstViewModel.inMemoryRepository).isSameAs(secondViewModel.inMemoryRepository)
        assertThat(firstViewModel.roomRepository).isInstanceOf(DefaultCartRepository::class.java)
        assertThat(firstViewModel.inMemoryRepository).isInstanceOf(InMemoryCartRepository::class.java)
        assertThat(firstViewModel.roomRepository)
            .isSameAs(appScope.resolve(CartRepository::class, RoomCart::class))
        assertThat(firstViewModel.inMemoryRepository)
            .isSameAs(appScope.resolve(CartRepository::class, InMemoryCart::class))
        assertThat(appScope.resolve(CartProductDao::class)).isSameAs(dao)
        val factory = ReflectionViewModelFactory(container)
        val factoryViewModel = factory.create(TestCartViewModel::class.java)
        assertThat(factoryViewModel.roomRepository).isSameAs(firstViewModel.roomRepository)
        assertThat(factoryViewModel.inMemoryRepository).isSameAs(firstViewModel.inMemoryRepository)
    }
}
