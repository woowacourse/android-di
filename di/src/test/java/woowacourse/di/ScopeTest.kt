package woowacourse.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ScopeTest {
    class TestRepository

    @Test
    fun `같은 스코프에서 의존성을 재사용한다`() {
        val container = DependencyContainer()
        val screen = ScopeType("screen")

        container.bind(TestRepository::class, screen) { TestRepository() }
        val scope = container.openScope(screen, "cart-1")

        val first = scope.resolve(TestRepository::class)
        val second = scope.resolve(TestRepository::class)

        assertThat(first).isSameAs(second)
    }

    @Test
    fun `다른 소유자의 스코프에서는 의존성을 분리한다`() {
        val container = DependencyContainer()
        val screen = ScopeType("screen")

        container.bind(TestRepository::class, screen) { TestRepository() }
        val firstScope = container.openScope(screen, "cart-1")
        val secondScope = container.openScope(screen, "cart-2")

        val first = firstScope.resolve(TestRepository::class)
        val second = secondScope.resolve(TestRepository::class)

        assertThat(first).isNotSameAs(second)
        assertThat(firstScope.resolve(TestRepository::class)).isSameAs(first)
        assertThat(secondScope.resolve(TestRepository::class)).isSameAs(second)
    }

    @Test
    fun `같은 종류와 소유자로 스코프를 다시 열면 기존 스코프를 반환한다`() {
        val container = DependencyContainer()
        val screen = ScopeType("screen")
        container.bind(TestRepository::class, screen) { TestRepository() }

        val firstScope = container.openScope(screen, "cart-1")
        val repository = firstScope.resolve(TestRepository::class)
        val reopenedScope = container.openScope(ScopeType("screen"), "cart-1")

        assertThat(reopenedScope).isSameAs(firstScope)
        assertThat(reopenedScope.resolve(TestRepository::class)).isSameAs(repository)
    }

    @Test
    fun `소유자 식별자가 같아도 종류가 다른 스코프의 의존성은 분리한다`() {
        val container = DependencyContainer()
        val screen = ScopeType("screen")
        val viewModel = ScopeType("viewModel")
        container.bind(TestRepository::class, screen) { TestRepository() }
        container.bind(TestRepository::class, viewModel) { TestRepository() }

        val screenScope = container.openScope(screen, "owner-1")
        val viewModelScope = container.openScope(viewModel, "owner-1")

        assertThat(screenScope.resolve(TestRepository::class))
            .isNotSameAs(viewModelScope.resolve(TestRepository::class))
    }
}
