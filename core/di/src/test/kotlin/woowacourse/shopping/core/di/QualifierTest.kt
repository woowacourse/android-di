package woowacourse.shopping.core.di

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class QualifierTest {
    private lateinit var dependencyContainer: DependencyContainer

    @Before
    fun resetDependencyContainer() {
        dependencyContainer = DependencyContainer()
    }

    @Test
    fun `Qualifier 이름이 일치하는 의존성이 생성자 주입된다`() {
        // given
        dependencyContainer.register(Repository::class, "default") { DefaultRepository() }

        val service = dependencyContainer.instance(ServiceWithQualifiedConstructor::class)

        // then
        assertThat(service.repository).isInstanceOf(DefaultRepository::class.java)
    }

    @Test
    fun `Qualifier 이름이 일치하는 의존성이 필드 주입된다`() {
        // given
        dependencyContainer.register(Repository::class, "default") { DefaultRepository() }

        val service = dependencyContainer.instance(ServiceWithQualifiedField::class)

        // then
        assertThat(service.repository).isInstanceOf(DefaultRepository::class.java)
    }

    @Test
    fun `등록되지 않은 Qualifier를 사용하면 예외가 발생한다`() {
        // given
        dependencyContainer.register(Repository::class, "default") { DefaultRepository() }

        assertThrows(IllegalStateException::class.java) {
            dependencyContainer.instance(ServiceWithUnknownQualifier::class)
        }
    }

    interface Repository

    class DefaultRepository : Repository

    class ServiceWithQualifiedConstructor
        @Inject
        constructor(
            @Qualifier("default") val repository: Repository,
        )

    class ServiceWithQualifiedField {
        @Inject
        @Qualifier("default")
        lateinit var repository: Repository
    }

    class ServiceWithUnknownQualifier {
        @Inject
        @Qualifier("unknown")
        lateinit var repository: Repository
    }
}
