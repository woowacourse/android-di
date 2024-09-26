package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.di.container.ApplicationDependencyContainer
import woowacourse.shopping.di.container.DependencyContainer
import javax.inject.Qualifier

class DependencyAutoInjectTest {
    private lateinit var dependencyContainer: DependencyContainer

    @Before
    fun setUp() {
        dependencyContainer = ApplicationDependencyContainer()
        DependencyInjector.initDependencyContainer(dependencyContainer)
    }

    @Test
    fun `알맞은 인스턴스를 주입한다`() {
        // given
        dependencyContainer.setDependency(
            ToBeInjected::class,
            FirstDependency::class,
            FirstQualifier::class,
        )

        dependencyContainer.setDependency(
            ToBeInjected::class,
            SecondDependency::class,
            SecondQualifier::class,
        )

        // when
        val actualInstance =
            DependencyInjector.createInstance(InjectOwner::class)

        // then
        assertThat(actualInstance).isInstanceOf(InjectOwner::class.java)
        assertThat(actualInstance.firstDependency).isInstanceOf(ToBeInjected::class.java)
        assertThat(actualInstance.secondDependency).isInstanceOf(ToBeInjected::class.java)
    }

    @Test
    fun `적절한 의존성을 찾을 수 없으면 예외가 발생한다`() {
        // when
        dependencyContainer.setDependency(
            ToBeInjected::class,
            FirstDependency::class,
            FirstQualifier::class,
        )

        // then
        assertThrows(IllegalArgumentException::class.java) {
            DependencyInjector.createInstance(InjectOwner::class)
        }
    }

    @Test
    fun `Qualifier가 올바르지 않아 적절한 의존성을 찾을 수 없으면 예외가 발생한다`() {
        // when
        dependencyContainer.setDependency(
            ToBeInjected::class,
            FirstDependency::class,
            ThirdQualifier::class,
        )

        dependencyContainer.setDependency(
            ToBeInjected::class,
            SecondDependency::class,
            SecondQualifier::class,
        )

        // then
        assertThrows(IllegalArgumentException::class.java) {
            DependencyInjector.createInstance(InjectOwner::class)
        }
    }
}
