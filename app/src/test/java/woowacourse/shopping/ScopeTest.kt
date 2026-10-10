package woowacourse.shopping

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.example.di.Scope
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.cart.ScreenScopeViewModel
import woowacourse.shopping.ui.products.ProductsViewModel
import woowacourse.shopping.util.annotations.InMemoryRepo
import woowacourse.shopping.util.annotations.RoomRepo

@RunWith(RobolectricTestRunner::class)
class ScopeTest {
    private val container get() = (RuntimeEnvironment.getApplication() as MyApplication).appContainer

    @Test
    fun `같은 스코프에서는 재사용하고 다른 소유자는 구분한다`() {
        val first = Scope("viewModel", container.appScope)
        val second = Scope("viewModel", container.appScope)
        val repository = container.di.resolve(ProductRepository::class, scope = first)

        assertThat(container.di.resolve(ProductRepository::class, scope = first)).isSameInstanceAs(repository)
        assertThat(container.di.resolve(ProductRepository::class, scope = second)).isNotSameInstanceAs(repository)
        first.close()
        second.close()
        assertThat(first.instanceCount).isEqualTo(0)
        assertThat(second.instanceCount).isEqualTo(0)
        assertThrows(IllegalStateException::class.java) {
            container.di.resolve(ProductRepository::class, scope = first)
        }
    }

    @Test
    fun `앱 저장소는 서로 다른 화면과 ViewModel에서 공유하고 Qualifier는 구분한다`() {
        val screen = Scope("screen", container.appScope)
        val viewModel = Scope("viewModel", container.appScope)
        val room = container.di.resolve(CartRepository::class, RoomRepo(), screen)
        val memory = container.di.resolve(CartRepository::class, InMemoryRepo(), screen)

        assertThat(container.di.resolve(CartRepository::class, RoomRepo(), viewModel)).isSameInstanceAs(room)
        assertThat(container.di.resolve(CartRepository::class, InMemoryRepo(), viewModel)).isSameInstanceAs(memory)
        assertThat(memory).isNotSameInstanceAs(room)
        screen.close()
        viewModel.close()
        assertThat(container.di.resolve(CartRepository::class, RoomRepo())).isSameInstanceAs(room)
    }

    @Test
    fun `ViewModelStore가 정리되면 ProductRepository 스코프를 종료한다`() {
        val store = ViewModelStore()
        val owner =
            object : ViewModelStoreOwner {
                override val viewModelStore = store
            }
        val provider = ViewModelProvider(owner, ViewModelFactory.viewModelFactory(RuntimeEnvironment.getApplication()))
        val first = provider["first", ProductsViewModel::class.java]
        val second = provider["second", ProductsViewModel::class.java]
        val scope = first.diScope

        assertThat(provider["first", ProductsViewModel::class.java]).isSameInstanceAs(first)
        assertThat(first.productRepository).isNotSameInstanceAs(second.productRepository)
        assertThat(container.di.resolve(ProductRepository::class, scope = scope)).isSameInstanceAs(first.productRepository)
        store.clear()
        assertThat(scope.isClosed).isTrue()
        assertThat(scope.instanceCount).isEqualTo(0)
        assertThat(second.diScope.isClosed).isTrue()
        assertThat(second.diScope.instanceCount).isEqualTo(0)
        val next = provider["first", ProductsViewModel::class.java]
        assertThat(next.productRepository).isNotSameInstanceAs(first.productRepository)
        store.clear()
        assertThat(next.diScope.instanceCount).isEqualTo(0)
    }

    @Test
    fun `화면 소유자가 유지되는 동안 포맷터를 재사용하고 종료 후 다시 진입하면 새로 생성한다`() {
        var previous: Any? = null
        repeat(20) {
            val store = ViewModelStore()
            val owner = ScreenScopeViewModel(container.appScope)
            store.put("screen", owner)
            val formatter = container.di.resolve(DateFormatter::class, scope = owner.scope)

            assertThat(container.di.resolve(DateFormatter::class, scope = owner.scope)).isSameInstanceAs(formatter)
            assertThat(formatter).isNotSameInstanceAs(previous)
            store.clear()
            assertThat(owner.scope.isClosed).isTrue()
            assertThat(owner.scope.instanceCount).isEqualTo(0)
            previous = formatter
        }
    }

    @Test
    fun `앱 스코프를 종료하면 CartRepository 참조를 제거한다`() {
        val appScope = container.appScope
        container.di.resolve(CartRepository::class, RoomRepo())
        container.di.resolve(CartRepository::class, InMemoryRepo())
        assertThat(appScope.instanceCount).isEqualTo(2)

        appScope.close()

        assertThat(appScope.isClosed).isTrue()
        assertThat(appScope.instanceCount).isEqualTo(0)
        assertThrows(IllegalStateException::class.java) {
            container.di.resolve(CartRepository::class, RoomRepo())
        }
    }
}
