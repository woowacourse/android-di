package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.di.DIContainer
import woowacourse.di.ScopeType
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.RoomCart

class AppScopeTest {
    private val container =
        DIContainer().apply {
            bind(
                type = CartRepository::class,
                implementation = InMemoryCartRepository::class,
                qualifier = RoomCart::class,
                scopeType = AppScope,
            )
        }
    private val appScopedContainer = container.openScope(AppScope, "test-application")

    @Test
    fun `서로 다른 하위 스코프에서 같은 CartRepository 인스턴스를 사용한다`() {
        val viewModelScope = ScopeType("test-view-model")
        val firstScope = appScopedContainer.openScope(viewModelScope, "first")
        val secondScope = appScopedContainer.openScope(viewModelScope, "second")

        val first = firstScope.get(CartRepository::class, RoomCart::class)
        val second = secondScope.get(CartRepository::class, RoomCart::class)

        assertThat(second).isSameInstanceAs(first)
    }
}
