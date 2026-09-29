package woowacourse.di

import org.assertj.core.api.Assertions
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

    @Test
    fun `요청한 타입의 인스턴스를 생성한다`() {
        val container = DependencyContainer()

        val repository = container.resolve(TestRepository::class)

        Assertions.assertThat(repository).isInstanceOf(TestRepository::class.java)
    }

    @Test
    fun `동일한 타입을 여러 번 요청하면 같은 인스턴스를 반환한다`() {
        val container = DependencyContainer()

        val repo1 = container.resolve(TestRepository::class)
        val repo2 = container.resolve(TestRepository::class)

        Assertions.assertThat(repo1).isSameAs(repo2)
    }

    @Test
    fun `생성자에 필요한 의존성을 자동으로 주입한다`() {
        val container = DependencyContainer()

        val service = container.resolve(TestService::class)
        val repository = container.resolve(TestRepository::class)

        Assertions.assertThat(service.repository).isSameAs(repository)
    }

    @Test
    fun `어노테이션이 붙은 필드에 의존성을 주입한다`() {
        val container = DependencyContainer()
        val service = AnnotationTestService()

        container.inject(service)

        val repository = container.resolve(TestRepository::class)

        Assertions.assertThat(service.testRepository).isSameAs(repository)
    }

    @Test
    fun `어노테이션이 붙지 않았으면 의존성을 주입하지 않는다`() {
        val container = DependencyContainer()
        val service = MixedAnnotationTestService()

        container.inject(service)

        val repository = container.resolve(TestRepository::class)

        Assertions.assertThat(service.testRepository).isNull()
        Assertions.assertThat(service.testAnnotationRepository).isSameAs(repository)
    }

    @Test
    fun `외부에서 생성한 인스턴스를 등록하면 해당 타입으로 반환한다`() {
        val container = DependencyContainer()
        val dao = FakeTestDao()

        container.register(TestDao::class, dao)

        val resolvedDao = container.resolve(TestDao::class)

        Assertions.assertThat(resolvedDao).isSameAs(dao)
    }

    @Test
    fun `등록된 의존성을 생성자에 재귀적으로 주입한다`() {
        val container = DependencyContainer()
        val dao = FakeTestDao()
        container.register(TestDao::class, dao)

        val repository = container.resolve(TestDaoRepository::class)

        Assertions.assertThat(repository.dao).isSameAs(dao)
    }
}
