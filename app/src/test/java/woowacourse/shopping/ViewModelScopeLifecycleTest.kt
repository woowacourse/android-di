package woowacourse.shopping

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.google.common.truth.Truth.assertThat
import io.github.firstwoosun.di.DependencyBinding
import io.github.firstwoosun.di.DependencyContainer
import io.github.firstwoosun.di.DependencyScope
import io.github.firstwoosun.di.InstanceProvider
import io.github.firstwoosun.di.ScopeKind
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.di.InMemory
import woowacourse.shopping.data.di.RoomBacked
import woowacourse.shopping.ui.di.ViewModelScopedViewModelFactory
import woowacourse.shopping.ui.products.ProductsViewModel

@RunWith(RobolectricTestRunner::class)
class ViewModelScopeLifecycleTest {
    private lateinit var container: DependencyContainer
    private lateinit var factory: ViewModelScopedViewModelFactory

    private val owners = mutableListOf<TestViewModelOwner>()

    @Before
    fun setUp() {
        container =
            DependencyContainer(
                instanceProvider = InstanceProvider { null },
                bindings =
                    listOf(
                        DependencyBinding(
                            type = CartRepository::class,
                            implementation = InMemoryCartRepository::class,
                            qualifier = RoomBacked::class,
                            scopeKind = ScopeKind.Application,
                        ),
                        DependencyBinding(
                            type = ProductRepository::class,
                            implementation = ProductRepository::class,
                            qualifier = InMemory::class,
                            scopeKind = ScopeKind.ViewModel,
                        ),
                    ),
            )

        factory = ViewModelScopedViewModelFactory(container)
    }

    @After
    fun tearDown() {
        owners.forEach { it.viewModelStore.clear() }
        container.close()
    }

    @Test
    fun `같은 저장소와 키에서는 ViewModel과 의존성을 재사용한다`() {
        val owner = newOwner()
        val provider = ViewModelProvider(owner, factory)

        val first = provider[ProductsViewModel::class.java]
        val second = provider[ProductsViewModel::class.java]

        assertThat(first).isSameInstanceAs(second)
        assertThat(first.productRepository)
            .isSameInstanceAs(second.productRepository)

        // Factory가 다시 호출되어 새 스코프를 만들지 않았는지도 확인
        assertThat(childScopes(container.applicationScope)).hasSize(1)
    }

    @Test
    fun `서로 다른 ViewModel은 ProductRepository를 분리하고 CartRepository를 공유한다`() {
        val firstProvider = ViewModelProvider(newOwner(), factory)
        val secondProvider = ViewModelProvider(newOwner(), factory)

        val first = firstProvider[ProductsViewModel::class.java]
        val second = secondProvider[ProductsViewModel::class.java]

        assertThat(first).isNotSameInstanceAs(second)
        assertThat(first.productRepository)
            .isNotSameInstanceAs(second.productRepository)
        assertThat(first.cartRepository)
            .isSameInstanceAs(second.cartRepository)
    }

    @Test
    fun `ViewModelStore를 비우면 실제 Factory가 만든 스코프가 닫힌다`() {
        val owner = newOwner()
        val provider = ViewModelProvider(owner, factory)

        provider[ProductsViewModel::class.java]

        val scope = childScopes(container.applicationScope).single()

        assertThat(scope.kind).isEqualTo(ScopeKind.ViewModel)
        assertThat(scope.isClosed).isFalse()

        // Android에서 ViewModel이 소멸하는 실제 통로
        owner.viewModelStore.clear()

        assertThat(scope.isClosed).isTrue()
        assertThat(childScopes(container.applicationScope)).isEmpty()
        assertThat(container.applicationScope.isClosed).isFalse()
    }

    @Test
    fun `ViewModel 정리 후 새로 생성하면 ProductRepository만 새로 만들어진다`() {
        val owner = newOwner()
        val provider = ViewModelProvider(owner, factory)

        val first = provider[ProductsViewModel::class.java]
        val firstProductRepository = first.productRepository
        val firstCartRepository = first.cartRepository

        owner.viewModelStore.clear()

        val second = provider[ProductsViewModel::class.java]

        assertThat(second).isNotSameInstanceAs(first)
        assertThat(second.productRepository)
            .isNotSameInstanceAs(firstProductRepository)
        assertThat(second.cartRepository)
            .isSameInstanceAs(firstCartRepository)
    }

    private fun newOwner(): TestViewModelOwner =
        TestViewModelOwner().also(owners::add)

    private class TestViewModelOwner : ViewModelStoreOwner {
        override val viewModelStore = ViewModelStore()
    }

    /**
     * 실제 Factory가 생성한 스코프를 관찰하는 테스트 전용 코드.
     * 프로덕션에 조회 API를 추가하지 않기 위해 reflection을 사용한다.
     */
    private fun childScopes(
        scope: DependencyScope,
    ): List<DependencyScope> {
        val field =
            DependencyScope::class.java
                .getDeclaredField("children")
                .apply { isAccessible = true }

        val children = field.get(scope) as Collection<*>

        return children.map { it as DependencyScope }
    }
}