package woowacourse.di

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue

class DiContainerTest {
    @Test
    fun `의존성의 스코프 종류와 현재 문맥으로 실제 보관함을 선택한다`() {
        val container = DiContainer()
        val appScope = ScopeType("app")
        val viewModelScope = ScopeType("view-model")
        container.openScope("app")
        container.openScope("vm-A")
        container.openScope("vm-B")
        container.registerScopeRule(MixedScopeConsumer::class, viewModelScope)
        container.registerScopeRule(Dependency::class, viewModelScope)
        container.registerScopeRule(SharedDependency::class, appScope)

        val first = container.instantiate(MixedScopeConsumer::class, scopeContext = scopeContext("vm-A"))
        val second = container.instantiate(MixedScopeConsumer::class, scopeContext = scopeContext("vm-B"))

        assertNotSame(first.dependency, second.dependency)
        assertSame(first.sharedDependency, second.sharedDependency)
    }

    @Test
    fun `Qualifier가 있는 정확한 스코프 규칙을 타입 공통 규칙보다 우선한다`() {
        val container = DiContainer()
        val appScope = ScopeType("app")
        val viewModelScope = ScopeType("view-model")
        container.openScope("app")
        container.openScope("vm-A")
        container.openScope("vm-B")
        container.registerInterfaceRule(Repository::class, DefaultRepository::class, TestRoom::class)
        container.registerScopeRule(Repository::class, viewModelScope)
        container.registerScopeRule(Repository::class, appScope, TestRoom::class)
        container.registerScopeRule(Dependency::class, appScope)

        val first =
            container.instantiate(Repository::class, TestRoom::class, scopeContext = scopeContext("vm-A"))
        val second =
            container.instantiate(Repository::class, TestRoom::class, scopeContext = scopeContext("vm-B"))

        assertSame(first, second)
    }

    @Test
    fun `정확한 Qualifier 스코프 규칙이 없으면 타입 공통 규칙을 사용한다`() {
        val container = DiContainer()
        val appScope = ScopeType("app")
        container.openScope("app")
        container.openScope("vm-A")
        container.openScope("vm-B")
        container.registerInterfaceRule(Repository::class, DefaultRepository::class, TestRoom::class)
        container.registerScopeRule(Repository::class, appScope)
        container.registerScopeRule(Dependency::class, appScope)

        val first =
            container.instantiate(Repository::class, TestRoom::class, scopeContext = scopeContext("vm-A"))
        val second =
            container.instantiate(Repository::class, TestRoom::class, scopeContext = scopeContext("vm-B"))

        assertSame(first, second)
    }

    @Test
    fun `스코프 규칙에 필요한 실제 ID가 현재 문맥에 없으면 오류를 낸다`() {
        val container = DiContainer()
        val viewModelScope = ScopeType("view-model")
        container.openScope("vm-A")
        container.registerScopeRule(Consumer::class, viewModelScope)
        container.registerScopeRule(Dependency::class, viewModelScope)

        val exception =
            assertFailsWith<IllegalArgumentException> {
                container.instantiate(
                    Consumer::class,
                    scopeContext = ScopeContext(scopeIds = emptyMap()),
                )
            }

        assertContains(exception.message.orEmpty(), "현재 생성 문맥에 스코프 ID가 없습니다: view-model")
    }

    @Test
    fun `scoped 요청에 스코프 규칙이 없으면 오류를 낸다`() {
        val container = DiContainer()
        container.openScope("vm-A")

        val exception =
            assertFailsWith<IllegalArgumentException> {
                container.instantiate(Dependency::class, scopeContext = scopeContext("vm-A"))
            }

        assertContains(exception.message.orEmpty(), "스코프 규칙이 없습니다.")
        assertContains(exception.message.orEmpty(), Dependency::class.qualifiedName.orEmpty())
    }

    @Test
    fun `Qualifier를 해석한 뒤 해당 규칙의 스코프를 선택한다`() {
        val container = DiContainer()
        val appScope = ScopeType("app")
        container.openScope("app")
        container.openScope("vm-A")
        container.openScope("vm-B")
        container.registerInterfaceRule(Repository::class, DefaultRepository::class, TestRoom::class)
        container.registerScopeRule(Repository::class, appScope, TestRoom::class)
        container.registerScopeRule(Dependency::class, appScope)

        val first = container.instantiate(Repository::class, scopeContext = scopeContext("vm-A"))
        val second = container.instantiate(Repository::class, scopeContext = scopeContext("vm-B"))

        assertSame(first, second)
    }

    @Test
    fun `외부 객체를 스코프 규칙에 따른 보관함에 등록한다`() {
        val container = DiContainer()
        val appScope = ScopeType("app")
        val dependency = Dependency()
        container.openScope("app")
        container.openScope("vm-A")
        container.openScope("vm-B")
        container.registerScopeRule(Dependency::class, appScope)

        container.register(Dependency::class, dependency, scopeContext = scopeContext("vm-A"))

        assertSame(
            dependency,
            container.instantiate(Dependency::class, scopeContext = scopeContext("vm-B")),
        )
    }

    @Test
    fun `필드 주입도 전달받은 문맥으로 스코프 보관함을 선택한다`() {
        val container = DiContainer()
        val appScope = ScopeType("app")
        val dependency = Dependency()
        val target = FieldInjectedTarget()
        container.openScope("app")
        container.openScope("vm-A")
        container.registerScopeRule(Dependency::class, appScope)
        container.register(Dependency::class, dependency, scopeContext = scopeContext("vm-A"))

        container.inject(target, scopeContext("vm-A"))

        assertSame(dependency, target.dependency)
    }

    @Test
    fun `닫힌 스코프의 핸들을 보관해도 내부 객체 참조는 남지 않는다`() {
        val container = DiContainer()
        val scope = container.openScope("screen-A")
        container.instantiate(Consumer::class, scopeId = "screen-A")
        assertTrue(scope.store.isNotEmpty())

        container.closeScope("screen-A")

        assertTrue(scope.store.isEmpty())
    }

    @Test
    fun `같은 스코프에서는 객체와 생성자 의존성을 재사용한다`() {
        val container = DiContainer()
        container.openScope("screen-A")

        val first = container.instantiate(Consumer::class, scopeId = "screen-A")
        val second = container.instantiate(Consumer::class, scopeId = "screen-A")
        val dependency = container.instantiate(Dependency::class, scopeId = "screen-A")

        assertSame(first, second)
        assertSame(first.dependency, dependency)
    }

    @Test
    fun `서로 다른 스코프에서는 객체와 생성자 의존성을 분리한다`() {
        val container = DiContainer()
        container.openScope("screen-A")
        container.openScope("screen-B")

        val first = container.instantiate(Consumer::class, scopeId = "screen-A")
        val second = container.instantiate(Consumer::class, scopeId = "screen-B")
        val original = container.instantiate(Consumer::class)

        assertNotSame(first, second)
        assertNotSame(first.dependency, second.dependency)
        assertNotSame(first, original)
        assertNotSame(first.dependency, original.dependency)
    }

    @Test
    fun `스코프를 닫고 다시 열면 새 객체를 생성하고 다른 스코프는 유지한다`() {
        val container = DiContainer()
        container.openScope("screen-A")
        container.openScope("screen-B")
        val first = container.instantiate(Consumer::class, scopeId = "screen-A")
        val other = container.instantiate(Consumer::class, scopeId = "screen-B")

        container.closeScope("screen-A")
        container.openScope("screen-A")
        val reopened = container.instantiate(Consumer::class, scopeId = "screen-A")

        assertNotSame(first, reopened)
        assertNotSame(first.dependency, reopened.dependency)
        assertSame(other, container.instantiate(Consumer::class, scopeId = "screen-B"))
    }

    @Test
    fun `열리지 않았거나 닫힌 스코프에서는 객체를 생성할 수 없다`() {
        val container = DiContainer()

        assertFailsWith<IllegalArgumentException> {
            container.instantiate(Consumer::class, scopeId = "screen-A")
        }
        container.openScope("screen-A")
        container.closeScope("screen-A")
        assertFailsWith<IllegalArgumentException> {
            container.instantiate(Consumer::class, scopeId = "screen-A")
        }
    }

    @Test
    fun `스코프에서도 Qualifier에 따른 구현체를 분리하고 재사용한다`() {
        val container = DiContainer()
        container.registerInterfaceRule(Repository::class, DefaultRepository::class, TestRoom::class)
        container.registerInterfaceRule(Repository::class, InMemoryRepository::class, TestMemory::class)
        container.openScope("screen-A")

        val consumer = container.instantiate(QualifiedConstructorConsumer::class, scopeId = "screen-A")

        assertIs<DefaultRepository>(consumer.roomRepository)
        assertIs<InMemoryRepository>(consumer.memoryRepository)
        assertSame(
            consumer.roomRepository,
            container.instantiate(Repository::class, TestRoom::class, scopeId = "screen-A"),
        )
    }

    @Test
    fun `같은 식별자의 스코프를 다시 열면 기존 보관함을 유지한다`() {
        val container = DiContainer()

        val first = container.openScope("screen-A")
        val second = container.openScope("screen-A")

        assertSame(first, second)
    }

    @Test
    fun `서로 다른 식별자의 스코프는 별도 보관함을 갖는다`() {
        val container = DiContainer()

        val first = container.openScope("screen-A")
        val second = container.openScope("screen-B")

        assertNotSame(first, second)
    }

    @Test
    fun `닫은 스코프를 다시 열면 새 보관함을 생성하고 다른 스코프는 유지한다`() {
        val container = DiContainer()
        val first = container.openScope("screen-A")
        val other = container.openScope("screen-B")

        container.closeScope("screen-A")

        assertNotSame(first, container.openScope("screen-A"))
        assertSame(other, container.openScope("screen-B"))
    }

    @Test
    fun `스코프를 반복해서 닫아도 다른 보관함은 유지한다`() {
        val container = DiContainer()
        container.openScope("screen-A")
        val other = container.openScope("screen-B")

        container.closeScope("screen-A")
        container.closeScope("screen-A")

        assertSame(other, container.openScope("screen-B"))
    }

    @Test
    fun `생성자 의존성을 재귀적으로 생성한다`() {
        val container = DiContainer()

        val consumer = container.instantiate(Consumer::class)

        assertIs<Dependency>(consumer.dependency)
    }

    @Test
    fun `같은 타입을 직접 여러 번 요청하면 생성한 객체를 재사용한다`() {
        val container = DiContainer()

        val first = container.instantiate(Consumer::class)
        val second = container.instantiate(Consumer::class)

        assertSame(first, second)
    }

    @Test
    fun `인터페이스 규칙과 등록된 객체를 함께 사용한다`() {
        val container = DiContainer()
        val dependency = Dependency()
        container.register(Dependency::class, dependency)
        container.registerInterfaceRule(Repository::class, DefaultRepository::class)

        val consumer = container.instantiate(RepositoryConsumer::class)

        assertSame(dependency, consumer.repository.dependency)
    }

    @Test
    fun `Android 타입이 아닌 일반 객체의 필드에 주입한다`() {
        val container = DiContainer()
        val target = FieldInjectedTarget()

        container.inject(target)

        assertIs<Dependency>(target.dependency)
    }

    @Test
    fun `같은 인터페이스의 구현체를 Qualifier에 따라 생성자에 주입한다`() {
        val container = DiContainer()
        container.registerInterfaceRule(Repository::class, DefaultRepository::class, TestRoom::class)
        container.registerInterfaceRule(Repository::class, InMemoryRepository::class, TestMemory::class)

        val consumer = container.instantiate(QualifiedConstructorConsumer::class)

        assertIs<DefaultRepository>(consumer.roomRepository)
        assertIs<InMemoryRepository>(consumer.memoryRepository)
    }

    @Test
    fun `같은 인터페이스의 구현체를 Qualifier에 따라 필드에 주입한다`() {
        val container = DiContainer()
        container.registerInterfaceRule(Repository::class, DefaultRepository::class, TestRoom::class)
        container.registerInterfaceRule(Repository::class, InMemoryRepository::class, TestMemory::class)
        val target = QualifiedFieldTarget()

        container.inject(target)

        assertIs<DefaultRepository>(target.roomRepository)
        assertIs<InMemoryRepository>(target.memoryRepository)
    }

    @Test
    fun `후보가 하나이면 Qualifier가 없어도 해당 구현체를 선택한다`() {
        val container = DiContainer()
        container.registerInterfaceRule(Repository::class, InMemoryRepository::class, TestMemory::class)

        val repository = container.instantiate(Repository::class)

        assertIs<InMemoryRepository>(repository)
    }

    @Test
    fun `후보가 여러 개인데 Qualifier가 없으면 오류를 낸다`() {
        val container = DiContainer()
        container.registerInterfaceRule(Repository::class, DefaultRepository::class, TestRoom::class)
        container.registerInterfaceRule(Repository::class, InMemoryRepository::class, TestMemory::class)

        val exception = assertFailsWith<IllegalStateException> { container.instantiate(Repository::class) }

        assertContains(exception.message.orEmpty(), "구현체에 대한 구분자가 없습니다.")
        assertContains(exception.message.orEmpty(), Repository::class.qualifiedName.orEmpty())
    }

    @Test
    fun `등록되지 않은 Qualifier를 요청하면 요청 정보를 담은 오류를 낸다`() {
        val container = DiContainer()

        val exception =
            assertFailsWith<IllegalArgumentException> {
                container.instantiate(Repository::class, TestRoom::class)
            }

        assertContains(exception.message.orEmpty(), "등록되지 않은 Qualifier입니다.")
        assertContains(exception.message.orEmpty(), Repository::class.qualifiedName.orEmpty())
        assertContains(exception.message.orEmpty(), TestRoom::class.qualifiedName.orEmpty())
    }

    class Dependency

    class Consumer(
        val dependency: Dependency,
    )

    class SharedDependency

    class MixedScopeConsumer(
        val dependency: Dependency,
        val sharedDependency: SharedDependency,
    )

    interface Repository {
        val dependency: Dependency
    }

    class DefaultRepository(
        override val dependency: Dependency,
    ) : Repository

    class InMemoryRepository : Repository {
        override val dependency: Dependency = Dependency()
    }

    class RepositoryConsumer(
        val repository: Repository,
    )

    class FieldInjectedTarget {
        @FieldInject
        lateinit var dependency: Dependency
    }

    class QualifiedConstructorConsumer(
        @TestRoom val roomRepository: Repository,
        @TestMemory val memoryRepository: Repository,
    )

    class QualifiedFieldTarget {
        @FieldInject
        @TestRoom
        lateinit var roomRepository: Repository

        @FieldInject
        @TestMemory
        lateinit var memoryRepository: Repository
    }

    @Qualifier
    @Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class TestRoom

    @Qualifier
    @Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class TestMemory

    private fun scopeContext(viewModelScopeId: String): ScopeContext =
        ScopeContext(
            scopeIds =
                mapOf(
                    ScopeType("app") to "app",
                    ScopeType("view-model") to viewModelScopeId,
                ),
        )
}
