package di

import com.example.di.DependencyModule
import com.example.di.Injector
import com.google.common.truth.Truth.assertThat
import di.fixture.ConstructorInjectedClass
import di.fixture.FakeComponentA
import di.fixture.FieldInjectedClass
import di.fixture.FirstConstructorInjectedComponent
import di.fixture.FirstFieldInjectedComponent
import di.fixture.InjectedClass
import di.fixture.MultiQualifierInjectedClass
import di.fixture.MultipleDependency
import di.fixture.MultipleDependencyImplA
import di.fixture.MultipleDependencyImplB
import di.fixture.NonQualifierInjectedClass
import di.fixture.QualifierInjectedClass
import di.fixture.RecursiveConstructorInjectedClass
import di.fixture.RecursiveFieldInjectedClass
import di.fixture.SecondConstructorInjectedComponent
import di.fixture.SecondFieldInjectedComponent
import di.fixture.SingleDependency
import di.fixture.SingleDependencyImpl
import org.junit.After
import org.junit.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows

class DependencyInjectionTest {
    private val dependencyModule = DependencyModule()

    @After
    fun tearDown() {
        dependencyModule.clear()
    }

    @Test
    fun `생성자에 적절한 의존성을 주입하여 객체 인스턴스를 생성한다`() {
        // given
        val injected = FakeComponentA()
        dependencyModule.addInstanceDependency(injected::class to injected)

        // when
        val result = Injector(dependencyModule).inject(ConstructorInjectedClass::class)

        // then
        val actual = result.constructorInjected
        assertThat(actual).isEqualTo(injected)
    }

    @Test
    fun `필드에 적절한 의존성을 주입하여 객체 인스턴스를 생성한다`() {
        // given
        val injected = FakeComponentA()
        dependencyModule.addInstanceDependency(injected::class to injected)

        // when
        val result = Injector(dependencyModule).inject(FieldInjectedClass::class)

        // then
        val actual = result.fieldInjected
        assertThat(actual).isEqualTo(injected)
    }

    @Test
    fun `생성자에 재귀적으로 의존성을 주입하여 객체를 생성한다`() {
        // given
        val secondInjected = SecondConstructorInjectedComponent()
        val firstInjected = FirstConstructorInjectedComponent(secondInjected)
        dependencyModule.addInstanceDependency(
            secondInjected::class to secondInjected,
            firstInjected::class to firstInjected,
        )

        // when
        val result = Injector(dependencyModule).inject(RecursiveConstructorInjectedClass::class)

        // then
        assertAll(
            {
                val actual = result.constructorInjected
                assertThat(actual).isEqualTo(firstInjected)
            },
            {
                val actual = result.constructorInjected.constructorInjected
                assertThat(actual).isEqualTo(secondInjected)
            },
        )
    }

    @Test
    fun `필드에 재귀적으로 의존성을 주입하여 객체를 생성한다`() {
        // given
        val secondInjected = SecondFieldInjectedComponent()
        val firstInjected = FirstFieldInjectedComponent().apply { fieldInjected = secondInjected }
        dependencyModule.addInstanceDependency(
            secondInjected::class to secondInjected,
            firstInjected::class to firstInjected,
        )

        // when
        val result = Injector(dependencyModule).inject(RecursiveFieldInjectedClass::class)

        // then
        assertAll(
            {
                val actual = result.fieldInjected
                assertThat(actual).isEqualTo(firstInjected)
            },
            {
                val actual = result.fieldInjected.fieldInjected
                assertThat(actual).isEqualTo(secondInjected)
            },
        )
    }

    @Test
    fun `인터페이스와 구현체가 일치하여 생성자 주입이 성공하는 경우`() {
        // given
        val injected = SingleDependencyImpl()
        dependencyModule.addInstanceDependency(SingleDependency::class to injected)

        // when
        val result = Injector(dependencyModule).inject(InjectedClass::class)

        // then
        val actual = result.dependency
        assertThat(actual).isEqualTo(injected)
    }

    @Test
    fun `중복된 구현체로 인해 생성자 주입이 실패하는 경우`() {
        // given
        dependencyModule.addDeferredDependency(
            MultipleDependency::class to MultipleDependencyImplA::class,
            MultipleDependency::class to MultipleDependencyImplB::class,
        )

        // then
        assertThrows<IllegalStateException> { Injector(dependencyModule).inject(NonQualifierInjectedClass::class) }
    }

    @Test
    fun `명시적 Qualifier에 의해 정확한 구현체가 주입되는 경우`() {
        // given
        dependencyModule.addDeferredDependency(
            MultipleDependency::class to MultipleDependencyImplA::class,
            MultipleDependency::class to MultipleDependencyImplB::class,
        )

        // when
        val result = Injector(dependencyModule).inject(QualifierInjectedClass::class)

        // then
        val actual = result.dependencyA
        assertThat(actual).isInstanceOf(MultipleDependencyImplA::class.java)
    }

    @Test
    fun `복수의 Qualifier에 의해 각각 다른 의존성이 올바르게 주입되는 경우`() {
        // given
        dependencyModule.addDeferredDependency(
            MultipleDependency::class to MultipleDependencyImplA::class,
            MultipleDependency::class to MultipleDependencyImplB::class,
        )

        // when
        val result = Injector(dependencyModule).inject(MultiQualifierInjectedClass::class)

        // then
        assertAll(
            {
                val actual = result.dependencyA
                assertThat(actual).isInstanceOf(MultipleDependencyImplA::class.java)
            },
            {
                val actual = result.dependencyB
                assertThat(actual).isInstanceOf(MultipleDependencyImplB::class.java)
            },
        )
    }
}
