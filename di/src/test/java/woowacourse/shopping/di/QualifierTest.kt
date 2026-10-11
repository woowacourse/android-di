@file:Suppress("NonAsciiCharacters")

package woowacourse.shopping.di

import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.assertj.core.api.Assertions.assertThat
import woowacourse.di.DependencyContainer
import woowacourse.di.Inject
import woowacourse.di.Qualifier

class QualifierTest {
    private val testScopeId = 1L

    interface Repository

    class FirstRepository : Repository

    class SecondRepository : Repository

    class FieldInjectionViewModel {
        @Inject
        @First
        lateinit var repository: Repository
    }

    class ConstructorInjectionViewModel(
        @param:First
        val repository: Repository,
    )

    class ViewModelWithoutQualifier {
        @Inject
        lateinit var repository: Repository
    }

    class ViewModelWithUnknownQualifier {
        @Inject
        @Unknown
        lateinit var repository: Repository
    }

    @Qualifier
    @Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class First

    @Qualifier
    @Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Second

    @Qualifier
    @Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Unknown

    @Before
    fun openScope() {
        DependencyContainer.openScope(testScopeId)
    }

    @After
    fun closeScope() {
        DependencyContainer.closeScope(testScopeId)
    }

    @Test
    fun `필드의 Qualifier에 맞는 구현체를 주입한다`() {
        DependencyContainer.register(
            testScopeId,
            Repository::class,
            First::class,
            FirstRepository(),
        )

        DependencyContainer.register(
            testScopeId,
            Repository::class,
            Second::class,
            SecondRepository(),
        )

        val viewModel =
            DependencyContainer.create(FieldInjectionViewModel::class, testScopeId)
                as FieldInjectionViewModel

        assertThat(viewModel.repository)
            .isInstanceOf(FirstRepository::class.java)
    }

    @Test
    fun `생성자 파라미터의 Qualifier에 맞는 구현체를 주입한다`() {
        DependencyContainer.register(
            testScopeId,
            Repository::class,
            First::class,
            FirstRepository(),
        )

        DependencyContainer.register(
            testScopeId,
            Repository::class,
            Second::class,
            SecondRepository(),
        )

        val viewModel =
            DependencyContainer.create(ConstructorInjectionViewModel::class, testScopeId)
                as ConstructorInjectionViewModel

        assertThat(viewModel.repository)
            .isInstanceOf(FirstRepository::class.java)
    }

    @Test
    fun `같은 타입의 구현체가 여러 개이고 Qualifier가 없으면 예외가 발생한다`() {
        DependencyContainer.register(
            testScopeId,
            Repository::class,
            First::class,
            FirstRepository(),
        )

        DependencyContainer.register(
            testScopeId,
            Repository::class,
            Second::class,
            SecondRepository(),
        )

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                DependencyContainer.create(ViewModelWithoutQualifier::class, testScopeId)
            }

        assertThat(exception)
            .hasMessageContaining("Qualifier")
    }

    @Test
    fun `등록되지 않은 Qualifier를 요청하면 예외가 발생한다`() {
        DependencyContainer.register(
            testScopeId,
            Repository::class,
            First::class,
            FirstRepository(),
        )

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                DependencyContainer.create(ViewModelWithUnknownQualifier::class, testScopeId)
            }

        assertThat(exception)
            .hasMessageContaining("등록되지 않았습니다")
    }
}
