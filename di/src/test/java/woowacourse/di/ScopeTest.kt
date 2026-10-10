package woowacourse.di

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class ScopeTest {
    class TestRepository

    class TestCartRepository

    class TestService(
        val cartRepository: TestCartRepository,
        val repository: TestRepository,
    )

    class TestFieldInjectionService {
        @MyInject
        lateinit var cartRepository: TestCartRepository

        @MyInject
        lateinit var repository: TestRepository

        var unannotatedRepository: TestRepository? = null
    }

    class TestSingleFieldInjectionService {
        @MyInject
        lateinit var repository: TestRepository
    }

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

    @Test
    fun `서로 다른 자식 스코프에서 앱 의존성을 공유한다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        val viewModel = ScopeType("viewModel")

        container.bind(TestCartRepository::class, app) {
            TestCartRepository()
        }

        val appScope = container.openScope(app, "application")
        val firstScope =
            container.openScope(viewModel, "vm-1", parent = appScope)
        val secondScope =
            container.openScope(viewModel, "vm-2", parent = appScope)

        val first = firstScope.resolve(TestCartRepository::class)
        val second = secondScope.resolve(TestCartRepository::class)

        assertThat(first).isSameAs(second)
        assertThat(first).isSameAs(
            appScope.resolve(TestCartRepository::class),
        )
    }

    @Test
    fun `앱 의존성은 공유하고 ViewModel 의존성은 분리한다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        val viewModel = ScopeType("viewModel")

        container.bind(TestCartRepository::class, app) {
            TestCartRepository()
        }
        container.bind(TestRepository::class, viewModel) {
            TestRepository()
        }

        val appScope = container.openScope(app, "application")
        val firstScope = container.openScope(viewModel, "vm-1", parent = appScope)
        val secondScope = container.openScope(viewModel, "vm-2", parent = appScope)

        val firstCart = firstScope.resolve(TestCartRepository::class)
        val secondCart = secondScope.resolve(TestCartRepository::class)
        val firstRepository = firstScope.resolve(TestRepository::class)
        val secondRepository = secondScope.resolve(TestRepository::class)

        assertThat(firstCart).isSameAs(secondCart)
        assertThat(firstCart)
            .isSameAs(appScope.resolve(TestCartRepository::class))

        assertThat(firstRepository).isNotSameAs(secondRepository)

        assertThat(firstScope.resolve(TestRepository::class))
            .isSameAs(firstRepository)
        assertThat(secondScope.resolve(TestRepository::class))
            .isSameAs(secondRepository)
    }

    @Test
    fun `생성자에 앱 의존성과 ViewModel 의존성을 주입한다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        val viewModel = ScopeType("viewModel")

        container.bind(TestCartRepository::class, app) {
            TestCartRepository()
        }
        container.bind(TestRepository::class, viewModel) {
            TestRepository()
        }
        container.bind(TestService::class, viewModel)

        val appScope = container.openScope(app, "application")
        val vmScope = container.openScope(viewModel, "vm-1", parent = appScope)

        val service = vmScope.resolve(TestService::class)

        assertThat(service.cartRepository)
            .isSameAs(appScope.resolve(TestCartRepository::class))
        assertThat(service.repository)
            .isSameAs(vmScope.resolve(TestRepository::class))
    }

    @Test
    fun `필드에 앱 의존성과 ViewModel 의존성을 주입한다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        val viewModel = ScopeType("viewModel")
        container.bind(TestCartRepository::class, app) { TestCartRepository() }
        container.bind(TestRepository::class, viewModel) { TestRepository() }
        container.bind(TestFieldInjectionService::class, viewModel)
        val appScope = container.openScope(app, "application")
        val firstScope = container.openScope(viewModel, "vm-1", parent = appScope)
        val secondScope = container.openScope(viewModel, "vm-2", parent = appScope)

        val first = firstScope.resolve(TestFieldInjectionService::class)
        val second = secondScope.resolve(TestFieldInjectionService::class)

        assertThat(first.cartRepository).isSameAs(appScope.resolve(TestCartRepository::class))
        assertThat(second.cartRepository).isSameAs(first.cartRepository)
        assertThat(first.repository).isSameAs(firstScope.resolve(TestRepository::class))
        assertThat(second.repository).isSameAs(secondScope.resolve(TestRepository::class))
        assertThat(first.repository).isNotSameAs(second.repository)
        assertThat(first.unannotatedRepository).isNull()
    }

    @Test
    fun `캐시된 객체를 조회할 때 필드를 다시 주입하지 않는다`() {
        val container = DependencyContainer()
        val viewModel = ScopeType("viewModel")
        container.bind(TestRepository::class, viewModel) { TestRepository() }
        container.bind(TestSingleFieldInjectionService::class, viewModel)
        val scope = container.openScope(viewModel, "vm-1")
        val service = scope.resolve(TestSingleFieldInjectionService::class)
        val replacement = TestRepository()
        service.repository = replacement

        val cachedService = scope.resolve(TestSingleFieldInjectionService::class)

        assertThat(cachedService).isSameAs(service)
        assertThat(cachedService.repository).isSameAs(replacement)
    }

    @Test
    fun `필드 주입에 실패한 객체는 캐시하지 않는다`() {
        val container = DependencyContainer()
        val viewModel = ScopeType("viewModel")
        var creationCount = 0
        container.bind(TestSingleFieldInjectionService::class, viewModel) {
            creationCount++
            TestSingleFieldInjectionService()
        }
        val scope = container.openScope(viewModel, "vm-1")

        assertThatThrownBy {
            scope.resolve(TestSingleFieldInjectionService::class)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining(TestRepository::class.qualifiedName)

        container.bind(TestRepository::class, viewModel) { TestRepository() }
        val service = scope.resolve(TestSingleFieldInjectionService::class)

        assertThat(creationCount).isEqualTo(2)
        assertThat(service.repository).isSameAs(scope.resolve(TestRepository::class))
    }

    @Test
    fun `등록하지 않은 의존성을 자동 생성하지 않고 실패한다`() {
        val container = DependencyContainer()
        val scope = container.openScope(ScopeType("app"), "application")

        assertThatThrownBy {
            scope.resolve(TestRepository::class)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining(TestRepository::class.qualifiedName)
    }

    @Test
    fun `형제 스코프에만 등록된 의존성을 조회할 수 없다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        val viewModel = ScopeType("viewModel")
        val screen = ScopeType("screen")
        container.bind(TestRepository::class, viewModel) { TestRepository() }
        val appScope = container.openScope(app, "application")
        val vmScope = container.openScope(viewModel, "vm-1", parent = appScope)
        val screenScope = container.openScope(screen, "cart-1", parent = appScope)
        vmScope.resolve(TestRepository::class)

        assertThatThrownBy {
            screenScope.resolve(TestRepository::class)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining(TestRepository::class.qualifiedName)
    }

    @Test
    fun `부모 스코프 객체의 필드에 자식 스코프 의존성을 주입할 수 없다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        val viewModel = ScopeType("viewModel")
        container.bind(TestSingleFieldInjectionService::class, app)
        container.bind(TestRepository::class, viewModel) { TestRepository() }
        val appScope = container.openScope(app, "application")
        val vmScope = container.openScope(viewModel, "vm-1", parent = appScope)

        assertThatThrownBy {
            vmScope.resolve(TestSingleFieldInjectionService::class)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining(TestRepository::class.qualifiedName)
    }

    @Test
    fun `스코프를 닫으면 캐시와 컨테이너의 스코프 참조를 제거한다`() {
        val container = DependencyContainer()
        val screen = ScopeType("screen")
        container.bind(TestRepository::class, screen) { TestRepository() }
        val scope = container.openScope(screen, "cart-1")
        scope.resolve(TestRepository::class)
        assertThat(scope.instanceCount).isEqualTo(1)
        assertThat(container.activeScopeCount).isEqualTo(1)

        scope.close()

        assertThat(scope.instanceCount).isZero()
        assertThat(container.activeScopeCount).isZero()
    }

    @Test
    fun `닫힌 스코프는 조회할 수 없고 같은 식별자로 다시 열면 새 인스턴스를 생성한다`() {
        val container = DependencyContainer()
        val screen = ScopeType("screen")
        container.bind(TestRepository::class, screen) { TestRepository() }
        val scope = container.openScope(screen, "cart-1")
        val repository = scope.resolve(TestRepository::class)

        scope.close()
        val reopenedScope = container.openScope(screen, "cart-1")
        val newRepository = reopenedScope.resolve(TestRepository::class)

        assertThat(reopenedScope).isNotSameAs(scope)
        assertThat(newRepository).isNotSameAs(repository)
        assertThat(reopenedScope.resolve(TestRepository::class)).isSameAs(newRepository)
        assertThatThrownBy {
            scope.resolve(TestRepository::class)
        }.isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("닫힌 스코프")
    }

    @Test
    fun `스코프를 반복해서 닫아도 같은 식별자로 다시 연 스코프는 유지된다`() {
        val container = DependencyContainer()
        val screen = ScopeType("screen")
        container.bind(TestRepository::class, screen) { TestRepository() }
        val scope = container.openScope(screen, "cart-1")
        scope.resolve(TestRepository::class)

        scope.close()
        scope.close()

        assertThat(scope.instanceCount).isZero()
        assertThat(container.activeScopeCount).isZero()

        val reopenedScope = container.openScope(screen, "cart-1")
        val repository = reopenedScope.resolve(TestRepository::class)

        scope.close()

        assertThat(container.activeScopeCount).isEqualTo(1)
        assertThat(container.openScope(screen, "cart-1")).isSameAs(reopenedScope)
        assertThat(reopenedScope.resolve(TestRepository::class)).isSameAs(repository)
    }

    @Test
    fun `자식 스코프를 닫아도 부모와 형제 스코프의 인스턴스는 유지된다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        val viewModel = ScopeType("viewModel")
        container.bind(TestCartRepository::class, app) { TestCartRepository() }
        container.bind(TestRepository::class, viewModel) { TestRepository() }
        val appScope = container.openScope(app, "application")
        val firstScope = container.openScope(viewModel, "vm-1", parent = appScope)
        val secondScope = container.openScope(viewModel, "vm-2", parent = appScope)
        val cartRepository = firstScope.resolve(TestCartRepository::class)
        firstScope.resolve(TestRepository::class)
        val secondRepository = secondScope.resolve(TestRepository::class)

        firstScope.close()

        assertThat(firstScope.instanceCount).isZero()
        assertThat(container.activeScopeCount).isEqualTo(2)
        assertThat(container.openScope(app, "application")).isSameAs(appScope)
        assertThat(container.openScope(viewModel, "vm-2", parent = appScope)).isSameAs(secondScope)
        assertThat(appScope.resolve(TestCartRepository::class)).isSameAs(cartRepository)
        assertThat(secondScope.resolve(TestCartRepository::class)).isSameAs(cartRepository)
        assertThat(secondScope.resolve(TestRepository::class)).isSameAs(secondRepository)
    }
}
