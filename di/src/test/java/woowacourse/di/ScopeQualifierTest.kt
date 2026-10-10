package woowacourse.di

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class ScopeQualifierTest {
    interface CartRepository

    class RoomRepository : CartRepository

    class InMemoryRepository : CartRepository

    @Qualifier
    @Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RoomCart

    @Qualifier
    @Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class InMemoryCart

    class QualifiedFieldService {
        @MyInject
        @RoomCart
        lateinit var repository: CartRepository
    }

    class QualifiedConstructorService(
        @RoomCart val repository: CartRepository,
    )

    class UnqualifiedFieldService {
        @MyInject
        lateinit var repository: CartRepository
    }

    @Test
    fun `같은 스코프의 구현체를 Qualifier로 구분하고 각각 재사용한다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        container.bind(CartRepository::class, app, RoomCart::class) { RoomRepository() }
        container.bind(CartRepository::class, app, InMemoryCart::class) { InMemoryRepository() }
        val scope = container.openScope(app, "application")

        val room = scope.resolve(CartRepository::class, RoomCart::class)
        val inMemory = scope.resolve(CartRepository::class, InMemoryCart::class)

        assertThat(room).isInstanceOf(RoomRepository::class.java)
        assertThat(inMemory).isInstanceOf(InMemoryRepository::class.java)
        assertThat(room).isNotSameAs(inMemory)
        assertThat(scope.resolve(CartRepository::class, RoomCart::class)).isSameAs(room)
        assertThat(scope.resolve(CartRepository::class, InMemoryCart::class)).isSameAs(inMemory)
    }

    @Test
    fun `필드의 Qualifier로 부모 스코프의 구현체를 선택한다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        val viewModel = ScopeType("viewModel")
        container.bind(CartRepository::class, app, RoomCart::class) { RoomRepository() }
        container.bind(CartRepository::class, app, InMemoryCart::class) { InMemoryRepository() }
        container.bind(QualifiedFieldService::class, viewModel)
        val appScope = container.openScope(app, "application")
        val firstScope = container.openScope(viewModel, "vm-1", parent = appScope)
        val secondScope = container.openScope(viewModel, "vm-2", parent = appScope)

        val first = firstScope.resolve(QualifiedFieldService::class)
        val second = secondScope.resolve(QualifiedFieldService::class)

        assertThat(first.repository).isSameAs(appScope.resolve(CartRepository::class, RoomCart::class))
        assertThat(second.repository).isSameAs(first.repository)
    }

    @Test
    fun `생성자 파라미터의 Qualifier로 구현체를 선택한다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        container.bind(CartRepository::class, app, RoomCart::class) { RoomRepository() }
        container.bind(CartRepository::class, app, InMemoryCart::class) { InMemoryRepository() }
        container.bind(QualifiedConstructorService::class, app)
        val scope = container.openScope(app, "application")

        val service = scope.resolve(QualifiedConstructorService::class)

        assertThat(service.repository).isSameAs(scope.resolve(CartRepository::class, RoomCart::class))
    }

    @Test
    fun `Qualifier 없는 모호한 요청은 어떤 구현체도 생성하지 않고 실패한다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        var creationCount = 0
        container.bind(CartRepository::class, app, RoomCart::class) {
            creationCount++
            RoomRepository()
        }
        container.bind(CartRepository::class, app, InMemoryCart::class) {
            creationCount++
            InMemoryRepository()
        }
        val scope = container.openScope(app, "application")

        assertThatThrownBy {
            scope.resolve(CartRepository::class)
        }.isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining(CartRepository::class.qualifiedName)
            .hasMessageContaining(RoomCart::class.qualifiedName)
            .hasMessageContaining(InMemoryCart::class.qualifiedName)
        assertThat(creationCount).isZero()
    }

    @Test
    fun `하나의 구현체만 캐시되어 있어도 Qualifier 없는 모호한 요청은 실패한다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        container.bind(CartRepository::class, app, RoomCart::class) { RoomRepository() }
        container.bind(CartRepository::class, app, InMemoryCart::class) { InMemoryRepository() }
        val scope = container.openScope(app, "application")
        scope.resolve(CartRepository::class, RoomCart::class)

        assertThatThrownBy {
            scope.resolve(CartRepository::class)
        }.isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `등록된 구현체가 하나면 Qualifier 없이 같은 인스턴스를 조회한다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        container.bind(CartRepository::class, app, RoomCart::class) { RoomRepository() }
        val scope = container.openScope(app, "application")

        val unqualified = scope.resolve(CartRepository::class)
        val qualified = scope.resolve(CartRepository::class, RoomCart::class)

        assertThat(unqualified).isInstanceOf(RoomRepository::class.java)
        assertThat(unqualified).isSameAs(qualified)
    }

    @Test
    fun `Qualifier 없는 필드에 여러 구현체가 있으면 주입에 실패한다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        container.bind(CartRepository::class, app, RoomCart::class) { RoomRepository() }
        container.bind(CartRepository::class, app, InMemoryCart::class) { InMemoryRepository() }
        container.bind(UnqualifiedFieldService::class, app)
        val scope = container.openScope(app, "application")

        assertThatThrownBy {
            scope.resolve(UnqualifiedFieldService::class)
        }.isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining(CartRepository::class.qualifiedName)
    }

    @Test
    fun `부모와 자식에 다른 Qualifier가 등록되어 있어도 모호한 요청은 실패한다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        val viewModel = ScopeType("viewModel")
        container.bind(CartRepository::class, app, RoomCart::class) { RoomRepository() }
        container.bind(CartRepository::class, viewModel, InMemoryCart::class) { InMemoryRepository() }
        val appScope = container.openScope(app, "application")
        val childScope = container.openScope(viewModel, "vm-1", parent = appScope)

        assertThatThrownBy {
            childScope.resolve(CartRepository::class)
        }.isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining(RoomCart::class.qualifiedName)
            .hasMessageContaining(InMemoryCart::class.qualifiedName)
    }

    @Test
    fun `등록되지 않은 Qualifier 요청은 다른 구현체로 대체하지 않는다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        container.bind(CartRepository::class, app, RoomCart::class) { RoomRepository() }
        val scope = container.openScope(app, "application")

        assertThatThrownBy {
            scope.resolve(CartRepository::class, InMemoryCart::class)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining(CartRepository::class.qualifiedName)
            .hasMessageContaining(InMemoryCart::class.qualifiedName)
    }

    @Test
    fun `자식에 다른 Qualifier가 있어도 지정한 부모 구현체를 주입한다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        val viewModel = ScopeType("viewModel")
        container.bind(CartRepository::class, app, RoomCart::class) { RoomRepository() }
        container.bind(CartRepository::class, viewModel, InMemoryCart::class) { InMemoryRepository() }
        container.bind(QualifiedFieldService::class, viewModel)
        val appScope = container.openScope(app, "application")
        val childScope = container.openScope(viewModel, "vm-1", parent = appScope)

        val service = childScope.resolve(QualifiedFieldService::class)

        assertThat(service.repository).isSameAs(appScope.resolve(CartRepository::class, RoomCart::class))
        assertThat(childScope.resolve(CartRepository::class, InMemoryCart::class))
            .isInstanceOf(InMemoryRepository::class.java)
    }

    @Test
    fun `Qualifier 없는 정의와 Qualifier 있는 정의가 함께 있으면 모호한 요청은 실패한다`() {
        val container = DependencyContainer()
        val app = ScopeType("app")
        container.bind(CartRepository::class, app) { InMemoryRepository() }
        container.bind(CartRepository::class, app, RoomCart::class) { RoomRepository() }
        val scope = container.openScope(app, "application")

        assertThatThrownBy {
            scope.resolve(CartRepository::class)
        }.isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("Qualifier 없음")
            .hasMessageContaining(RoomCart::class.qualifiedName)
    }

    @Test
    fun `생성 함수를 생략한 Qualifier 등록도 스코프별로 생성한다`() {
        val container = DependencyContainer()
        val viewModel = ScopeType("viewModel")
        container.bind(RoomRepository::class, viewModel, RoomCart::class)
        val firstScope = container.openScope(viewModel, "vm-1")
        val secondScope = container.openScope(viewModel, "vm-2")

        val first = firstScope.resolve(RoomRepository::class, RoomCart::class)
        val second = secondScope.resolve(RoomRepository::class, RoomCart::class)

        assertThat(first).isNotSameAs(second)
        assertThat(firstScope.resolve(RoomRepository::class)).isSameAs(first)
    }
}
