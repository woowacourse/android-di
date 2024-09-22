package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class DefaultDependencyContainerTest {
    private lateinit var dependencyContainer: DependencyContainer

    @Before
    fun setUp() {
        dependencyContainer = DefaultDependencyContainer()
    }

    @Test
    fun `Qualifier가 일치하지 않으면 알맞은 의존성을 받을 수 없다`() {
        // given
        dependencyContainer.setDependency(
            ToBeInjected::class,
            FirstDependency::class,
            "first",
        )

        val actual = dependencyContainer.getImplement<FirstDependency>(ToBeInjected::class, "second")

        assertThat(actual).isNull()
    }
}
