package woowacourse.di

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class DependencyContainerTest {
    class TestRepository

    interface TestDao

    class TestService(
        val repository: TestRepository,
    )

    class AnnotationTestService {
        @MyInject
        lateinit var testRepository: TestRepository
    }

    class MixedAnnotationTestService {
        @MyInject
        lateinit var testAnnotationRepository: TestRepository
        var testRepository: TestRepository? = null
    }

    class FakeTestDao : TestDao

    class TestDaoRepository(
        val dao: TestDao,
    )

    interface QualifiedTestRepository

    class RoomTestRepository : QualifiedTestRepository

    class InMemoryTestRepository : QualifiedTestRepository

    @Qualifier
    @Target(AnnotationTarget.PROPERTY)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RoomCart

    @Qualifier
    @Target(AnnotationTarget.PROPERTY)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class InMemoryCart

    @Test
    fun `요청한 타입의 인스턴스를 생성한다`() {
        val container = DependencyContainer()

        val repository = container.resolve(TestRepository::class)

        assertThat(repository).isInstanceOf(TestRepository::class.java)
    }

    @Test
    fun `동일한 타입을 여러 번 요청하면 같은 인스턴스를 반환한다`() {
        val container = DependencyContainer()

        val repo1 = container.resolve(TestRepository::class)
        val repo2 = container.resolve(TestRepository::class)

        assertThat(repo1).isSameAs(repo2)
    }

    @Test
    fun `생성자에 필요한 의존성을 자동으로 주입한다`() {
        val container = DependencyContainer()

        val service = container.resolve(TestService::class)
        val repository = container.resolve(TestRepository::class)

        assertThat(service.repository).isSameAs(repository)
    }

    @Test
    fun `어노테이션이 붙은 필드에 의존성을 주입한다`() {
        val container = DependencyContainer()
        val service = AnnotationTestService()

        container.inject(service)

        val repository = container.resolve(TestRepository::class)

        assertThat(service.testRepository).isSameAs(repository)
    }

    @Test
    fun `어노테이션이 붙지 않았으면 의존성을 주입하지 않는다`() {
        val container = DependencyContainer()
        val service = MixedAnnotationTestService()

        container.inject(service)

        val repository = container.resolve(TestRepository::class)

        assertThat(service.testRepository).isNull()
        assertThat(service.testAnnotationRepository).isSameAs(repository)
    }

    @Test
    fun `외부에서 생성한 인스턴스를 등록하면 해당 타입으로 반환한다`() {
        val container = DependencyContainer()
        val dao = FakeTestDao()

        container.register(TestDao::class, dao)

        val resolvedDao = container.resolve(TestDao::class)

        assertThat(resolvedDao).isSameAs(dao)
    }

    @Test
    fun `등록된 의존성을 생성자에 재귀적으로 주입한다`() {
        val container = DependencyContainer()
        val dao = FakeTestDao()
        container.register(TestDao::class, dao)

        val repository = container.resolve(TestDaoRepository::class)

        assertThat(repository.dao).isSameAs(dao)
    }

    @Test
    fun `같은 타입의 구현체를 qualifier로 구분해서 조회한다`() {
        val container = DependencyContainer()
        val roomTestRepository = RoomTestRepository()
        val inMemoryTestRepository = InMemoryTestRepository()

        container.register(
            type = QualifiedTestRepository::class,
            instance = roomTestRepository,
            qualifier = RoomCart::class,
        )
        container.register(
            type = QualifiedTestRepository::class,
            instance = inMemoryTestRepository,
            qualifier = InMemoryCart::class,
        )

        val resolvedRoomTestRepository =
            container.resolve(
                type = QualifiedTestRepository::class,
                qualifier = RoomCart::class,
            )

        val resolvedInMemoryTestRepository =
            container.resolve(
                type = QualifiedTestRepository::class,
                qualifier = InMemoryCart::class,
            )

        assertThat(resolvedInMemoryTestRepository).isSameAs(inMemoryTestRepository)
        assertThat(resolvedRoomTestRepository).isSameAs(roomTestRepository)
    }

    @Test
    fun `같은 타입의 의존성이 둘 이상이고 qualifier가 없으면 명확한 예외를 던진다`() {
        val container = DependencyContainer()

        container.register(
            type = QualifiedTestRepository::class,
            instance = RoomTestRepository(),
            qualifier = RoomCart::class,
        )
        container.register(
            type = QualifiedTestRepository::class,
            instance = InMemoryTestRepository(),
            qualifier = InMemoryCart::class,
        )
        assertThatThrownBy {
            container.resolve(QualifiedTestRepository::class)
        }.isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `등록된 구현체가 하나면 qualifier 없이 조회할 수 있다`() {
        val container = DependencyContainer()
        val repository = RoomTestRepository()

        container.register(
            type = QualifiedTestRepository::class,
            instance = repository,
            qualifier = RoomCart::class,
        )

        val resolvedRepository = container.resolve(QualifiedTestRepository::class)

        assertThat(resolvedRepository).isSameAs(repository)
    }

    class QualifiedInjectionService {
        @MyInject
        @RoomCart
        lateinit var repository: QualifiedTestRepository
    }

    class UnqualifiedInjectionService {
        @MyInject
        lateinit var repository: QualifiedTestRepository
    }

    @Test
    fun `qualifier가 붙은 필드에 지정한 구현체를 주입한다`() {
        val container = DependencyContainer()
        val roomTestRepository = RoomTestRepository()
        val inMemoryTestRepository = InMemoryTestRepository()
        val service = QualifiedInjectionService()

        container.register(
            type = QualifiedTestRepository::class,
            instance = roomTestRepository,
            qualifier = RoomCart::class,
        )

        container.register(
            type = QualifiedTestRepository::class,
            instance = inMemoryTestRepository,
            qualifier = InMemoryCart::class,
        )

        container.inject(service)

        assertThat(service.repository).isSameAs(roomTestRepository)
    }

    @Test
    fun `qualifier가 없는 필드의 후보가 여러 개면 예외를 던진다`() {
        val container = DependencyContainer()
        val service = UnqualifiedInjectionService()

        container.register(
            type = QualifiedTestRepository::class,
            instance = RoomTestRepository(),
            qualifier = RoomCart::class,
        )
        container.register(
            type = QualifiedTestRepository::class,
            instance = InMemoryTestRepository(),
            qualifier = InMemoryCart::class,
        )

        assertThatThrownBy {
            container.inject(service)
        }.isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining(QualifiedTestRepository::class.qualifiedName)
            .hasMessageContaining(RoomCart::class.qualifiedName)
            .hasMessageContaining(InMemoryCart::class.qualifiedName)
    }

    @Test
    fun `qualifier가 없는 필드의 후보가 하나면 해당 구현체를 주입한다`() {
        val container = DependencyContainer()
        val repository = RoomTestRepository()
        val service = UnqualifiedInjectionService()

        container.register(
            type = QualifiedTestRepository::class,
            instance = repository,
            qualifier = RoomCart::class,
        )

        container.inject(service)

        assertThat(service.repository).isSameAs(repository)
    }
}
