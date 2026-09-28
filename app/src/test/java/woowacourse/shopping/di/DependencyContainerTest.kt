package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class DependencyContainerTest {
    class TestRepository

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
}
