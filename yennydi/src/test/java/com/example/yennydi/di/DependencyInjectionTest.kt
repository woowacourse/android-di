package di

import com.example.yennydi.di.AbstractDependencyContainer
import com.example.yennydi.di.Injector
import com.example.yennydi.di.fixture.ConstructorInjectedClass
import com.example.yennydi.di.fixture.FakeComponentA
import com.example.yennydi.di.fixture.FieldInjectedClass
import com.example.yennydi.di.fixture.FirstConstructorInjectedComponent
import com.example.yennydi.di.fixture.FirstFieldInjectedComponent
import com.example.yennydi.di.fixture.InjectedClass
import com.example.yennydi.di.fixture.MultiQualifierInjectedClass
import com.example.yennydi.di.fixture.MultipleDependency
import com.example.yennydi.di.fixture.MultipleDependencyImplA
import com.example.yennydi.di.fixture.MultipleDependencyImplB
import com.example.yennydi.di.fixture.NonQualifierInjectedClass
import com.example.yennydi.di.fixture.QualifierInjectedClass
import com.example.yennydi.di.fixture.RecursiveConstructorInjectedClass
import com.example.yennydi.di.fixture.RecursiveFieldInjectedClass
import com.example.yennydi.di.fixture.SecondConstructorInjectedComponent
import com.example.yennydi.di.fixture.SecondFieldInjectedComponent
import com.example.yennydi.di.fixture.SingleDependency
import com.example.yennydi.di.fixture.SingleDependencyImpl
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.assertThrows

class FakeDependencyContainer : AbstractDependencyContainer()

class DependencyInjectionTest {
    private val container = FakeDependencyContainer()

    @After
    fun tearDown() {
        container.clear()
    }

    @Test
    fun `생성자에 적절한 의존성을 주입하여 객체 인스턴스를 생성한다`() {
        // given
        val injected = FakeComponentA()
        container.addInstance(injected::class, injected)

        // when
        val result = Injector(container).inject(ConstructorInjectedClass::class, container)

        // then
        val actual = result.constructorInjected
        assertThat(actual).isEqualTo(injected)
    }

    @Test
    fun `필드에 적절한 의존성을 주입하여 객체 인스턴스를 생성한다`() {
        // given
        val injected = FakeComponentA()
        container.addInstance(injected::class, injected)

        // when
        val result = Injector(container).inject(FieldInjectedClass::class, container)

        // then
        val actual = result.fieldInjected
        assertThat(actual).isEqualTo(injected)
    }

    @Test
    fun `생성자에 재귀적으로 의존성을 주입하여 객체를 생성한다`() {
        // given
        val secondInjected = SecondConstructorInjectedComponent()
        val firstInjected = FirstConstructorInjectedComponent(secondInjected)
        container.addInstance(secondInjected::class, secondInjected)
        container.addInstance(firstInjected::class, firstInjected)

        // when
        val result = Injector(container).inject(RecursiveConstructorInjectedClass::class, container)

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
        container.addInstance(secondInjected::class, secondInjected)
        container.addInstance(firstInjected::class, firstInjected)

        // when
        val result = Injector(container).inject(RecursiveFieldInjectedClass::class, container)

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
        container.addInstance(SingleDependency::class, injected)

        // when
        val result = Injector(container).inject(InjectedClass::class, container)

        // then
        val actual = result.dependency
        assertThat(actual).isEqualTo(injected)
    }

    @Test
    fun `중복된 구현체로 인해 생성자 주입이 실패하는 경우`() {
        // given
        container.addDeferredDependency(
            MultipleDependency::class to MultipleDependencyImplA::class,
            MultipleDependency::class to MultipleDependencyImplB::class,
        )

        val container = FakeDependencyContainer()

        // then
        assertThrows<IllegalArgumentException> {
            Injector(container).inject(
                NonQualifierInjectedClass::class,
                container,
            )
        }
    }

    @Test
    fun `명시적 Qualifier에 의해 정확한 구현체가 주입되는 경우`() {
        // given
        container.addDeferredDependency(
            MultipleDependency::class to MultipleDependencyImplA::class,
            MultipleDependency::class to MultipleDependencyImplB::class,
        )

        // when
        val result = Injector(container).inject(QualifierInjectedClass::class, container)

        // then
        val actual = result.dependencyA
        assertThat(actual).isInstanceOf(MultipleDependencyImplA::class.java)
    }

    @Test
    fun `복수의 Qualifier에 의해 각각 다른 의존성이 올바르게 주입되는 경우`() {
        // given
        container.addDeferredDependency(
            MultipleDependency::class to MultipleDependencyImplA::class,
            MultipleDependency::class to MultipleDependencyImplB::class,
        )

        // when
        val result = Injector(container).inject(MultiQualifierInjectedClass::class, container)

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
