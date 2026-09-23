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

    class TestViewModel {
        @Inject
        @First
        lateinit var repository: Repository
    }

    class TestViewModelWithoutQualifier {
        @Inject
        lateinit var repository: Repository
    }

    class TestViewModelWithUnknownQualifier {
        @Inject
        @Unknown
        lateinit var repository: Repository
    }

    @Qualifier
    @Target(AnnotationTarget.FIELD)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class First

    @Qualifier
    @Target(AnnotationTarget.FIELD)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Second

    @Qualifier
    @Target(AnnotationTarget.FIELD)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Unknown

    @Test
    fun `Qualifier가 지정된 구현체를 주입한다`() {
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

        val viewModel = DependencyContainer.create(TestViewModel::class) as TestViewModel

        assertThat(viewModel.repository).isInstanceOf(FirstRepository::class.java)
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
                DependencyContainer.create(TestViewModelWithoutQualifier::class)
            }

        assertThat(exception).hasMessageContaining("Qualifier")
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
                DependencyContainer.create(TestViewModelWithUnknownQualifier::class)
            }

        assertThat(exception)
            .hasMessageContaining("등록되지 않았습니다")
    }
}
