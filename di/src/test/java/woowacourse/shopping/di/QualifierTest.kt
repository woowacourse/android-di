@file:Suppress("NonAsciiCharacters")

package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test
import woowacourse.di.DependencyContainer
import woowacourse.di.Inject
import woowacourse.di.Qualifier

class QualifierTest {
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

    @Test
    fun `필드의 Qualifier에 맞는 구현체를 주입한다`() {
        DependencyContainer.register(
            Repository::class,
            First::class,
            FirstRepository(),
        )

        DependencyContainer.register(
            Repository::class,
            Second::class,
            SecondRepository(),
        )

        val viewModel =
            DependencyContainer.create(FieldInjectionViewModel::class)
                    as FieldInjectionViewModel

        assertThat(viewModel.repository)
            .isInstanceOf(FirstRepository::class.java)
    }

    @Test
    fun `생성자 파라미터의 Qualifier에 맞는 구현체를 주입한다`() {
        DependencyContainer.register(
            Repository::class,
            First::class,
            FirstRepository(),
        )

        DependencyContainer.register(
            Repository::class,
            Second::class,
            SecondRepository(),
        )

        val viewModel =
            DependencyContainer.create(ConstructorInjectionViewModel::class)
                    as ConstructorInjectionViewModel

        assertThat(viewModel.repository)
            .isInstanceOf(FirstRepository::class.java)
    }

    @Test
    fun `같은 타입의 구현체가 여러 개이고 Qualifier가 없으면 예외가 발생한다`() {
        DependencyContainer.register(
            Repository::class,
            First::class,
            FirstRepository(),
        )

        DependencyContainer.register(
            Repository::class,
            Second::class,
            SecondRepository(),
        )

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                DependencyContainer.create(ViewModelWithoutQualifier::class)
            }

        assertThat(exception)
            .hasMessageContaining("Qualifier")
    }

    @Test
    fun `등록되지 않은 Qualifier를 요청하면 예외가 발생한다`() {
        DependencyContainer.register(
            Repository::class,
            First::class,
            FirstRepository(),
        )

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                DependencyContainer.create(ViewModelWithUnknownQualifier::class)
            }

        assertThat(exception)
            .hasMessageContaining("등록되지 않았습니다")
    }
}