package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.di.container.ApplicationDependencyContainer
import woowacourse.shopping.di.container.DependencyContainer

class ApplicationDependencyContainerTest {
    private lateinit var dependencyContainer: DependencyContainer

    @Before
    fun setUp() {
        dependencyContainer = ApplicationDependencyContainer()
    }

    @Test
    fun `Qualifier가 일치하지 않으면 알맞은 의존성을 받을 수 없다`() {
        // given
        dependencyContainer.setDependency(
            ToBeInjected::class,
            FirstDependency::class,
            FirstQualifier::class,
        )

        val actual =
            dependencyContainer.getImplement<FirstDependency>(
                ToBeInjected::class,
                SecondQualifier::class,
            )

        assertThat(actual).isNull()
    }
}
