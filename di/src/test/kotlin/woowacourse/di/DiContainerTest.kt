package woowacourse.di

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotSame
import kotlin.test.assertSame

class DiContainerTest {
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
}
