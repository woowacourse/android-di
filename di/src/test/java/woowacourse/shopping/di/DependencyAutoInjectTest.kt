package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

interface ToBeInjected

class FirstDependency : ToBeInjected

class SecondDependency : ToBeInjected

class InjectOwner {
    @Qualifier("first")
    @property:FieldInject
    lateinit var firstDependency: FirstDependency

    @Qualifier("second")
    @property:FieldInject
    lateinit var secondDependency: SecondDependency
}

class DependencyAutoInjectTest {

    lateinit var fakeDependencyContainer: FakeDependencyContainer

    @Before
    fun setUp() {
        fakeDependencyContainer = FakeDependencyContainer()
        DependencyInjector.initDependencyContainer(fakeDependencyContainer)
    }

    @Test
    fun `알맞은 인스턴스를 주입한다`() {
        // given
        fakeDependencyContainer.setDependency(
            ToBeInjected::class, FirstDependency::class, "first"
        )
        fakeDependencyContainer.setDependency(
            ToBeInjected::class, SecondDependency::class, "second"
        )

        // when
        val actualInstance = DependencyInjector.createInstanceFromConstructor(InjectOwner::class.java)

        // then
        assertThat(actualInstance).isInstanceOf(InjectOwner::class.java)
        assertThat(actualInstance.firstDependency).isInstanceOf(ToBeInjected::class.java)
        assertThat(actualInstance.secondDependency).isInstanceOf(ToBeInjected::class.java)
    }
}
