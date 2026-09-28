package woowacourse.di

import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertSame

class DiContainerTest {
    @Test
    fun `생성자 의존성을 재귀적으로 생성한다`() {
        val container = DiContainer()

        val consumer = container.instantiate(Consumer::class)

        assertIs<Dependency>(consumer.dependency)
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

    class RepositoryConsumer(
        val repository: Repository,
    )

    class FieldInjectedTarget {
        @FieldInject
        lateinit var dependency: Dependency
    }
}
