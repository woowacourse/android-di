package woowacourse.shopping.core.di

import com.google.common.truth.Truth.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Before
import org.junit.Test

class QualifierTest {
    private lateinit var dependencyContainer: DependencyContainer

    @Before
    fun setUp() {
        dependencyContainer = DependencyContainer()
    }

    @Test
    fun `Qualifier 어노테이션이 일치하는 의존성이 생성자 주입된다`() {
        // given
        dependencyContainer.register(
            clazz = Repository::class,
            qualifier = DefaultQualifier::class,
        ) { DefaultRepository() }

        val service: ServiceWithQualifiedConstructor =
            dependencyContainer.instance(ServiceWithQualifiedConstructor::class)

        // then
        assertThat(service.repository).isInstanceOf(DefaultRepository::class.java)
    }

    @Test
    fun `Qualifier 어노테이션이 일치하는 의존성이 필드 주입된다`() {
        // given
        dependencyContainer.register(
            clazz = Repository::class,
            qualifier = DefaultQualifier::class,
        ) { DefaultRepository() }

        val service: ServiceWithQualifiedField =
            dependencyContainer.instance(ServiceWithQualifiedField::class)

        // then
        assertThat(service.repository).isInstanceOf(DefaultRepository::class.java)
    }

    @Test
    fun `등록되지 않은 Qualifier 어노테이션을 사용하면 예외가 발생한다`() {
        // given
        dependencyContainer.register(
            clazz = Repository::class,
            qualifier = DefaultQualifier::class,
        ) { DefaultRepository() }

        // then
        assertThatThrownBy {
            dependencyContainer.instance(ServiceWithUnknownQualifierField::class)
        }.hasMessage("No injectable constructor found for Repository")
    }

    interface Repository

    class DefaultRepository : Repository

    class ServiceWithQualifiedConstructor
        @Inject
        constructor(
            @DefaultQualifier val repository: Repository,
        )

    class ServiceWithQualifiedField {
        @Inject
        @DefaultQualifier
        lateinit var repository: Repository
    }

    class ServiceWithUnknownQualifierField {
        @Inject
        @FakeQualifier
        lateinit var repository: Repository
    }

    @Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
    @Qualifier
    annotation class DefaultQualifier

    @Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
    @Qualifier
    annotation class FakeQualifier
}
