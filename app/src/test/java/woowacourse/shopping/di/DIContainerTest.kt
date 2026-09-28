package woowacourse.shopping.di

import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class DIContainerTest {
    @Test
    fun `Inject 애노테이션이 붙은 필드에 의존성을 주입한다`() {
        // given
        val target = TestTarget()

        // when
        DIContainer.injectFields(target)

        // then
        assertTrue(target.isDependencyInitialized())
    }

    @Test
    fun `Inject 애노테이션이 붙은 여러 필드에 의존성을 주입한다`() {
        // given
        val target = MultipleDependencyTarget()

        // when
        DIContainer.injectFields(target)

        // then
        assertTrue(target.isFirstDependencyInitialized())
        assertTrue(target.isSecondDependencyInitialized())
    }

    @Test
    fun `Inject 애노테이션이 없는 필드는 주입하지 않는다`() {
        // given
        val target = NonInjectedTarget()

        // when
        DIContainer.injectFields(target)

        // then
        assertFalse(target.isDependencyInitialized())
    }

    @Test
    fun `순환 의존성이 있으면 예외가 발생한다`() {
        assertThrows(
            IllegalStateException::class.java,
        ) {
            DIContainer.createInstance(A::class)
        }
    }

    private class A(
        val b: B,
    )

    private class B(
        val a: A,
    )

    class TestDependency

    class FirstDependency

    class SecondDependency

    class TestTarget {
        @Inject
        lateinit var dependency: TestDependency

        fun isDependencyInitialized(): Boolean = ::dependency.isInitialized
    }

    class MultipleDependencyTarget {
        @Inject
        lateinit var firstDependency: FirstDependency

        @Inject
        lateinit var secondDependency: SecondDependency

        fun isFirstDependencyInitialized(): Boolean = ::firstDependency.isInitialized

        fun isSecondDependencyInitialized(): Boolean = ::secondDependency.isInitialized
    }

    class NonInjectedTarget {
        lateinit var dependency: TestDependency

        fun isDependencyInitialized(): Boolean = ::dependency.isInitialized
    }
}
