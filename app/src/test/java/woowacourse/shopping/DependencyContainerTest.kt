package woowacourse.shopping

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import woowacourse.shopping.di.DependencyContainer

class DependencyContainerTest {
    @Test
    fun `등록된 객체를 같은 인스턴스로 반환한다`() {
        val container = DependencyContainer()
        val dependency = RegisteredDependency()
        container.registerInstance(RegisteredDependency::class, dependency)

        assertThat(container.get(RegisteredDependency::class)).isSameAs(dependency)
    }

    @Test
    fun `생성자 의존성을 재귀적으로 만들고 공유한다`() {
        val container = DependencyContainer()
        val dependency = RegisteredDependency()
        container.registerInstance(RegisteredDependency::class, dependency)

        val first = container.get(IntermediateDependency::class)
        val second = container.get(IntermediateDependency::class)

        assertThat(first.dependency).isSameAs(dependency)
        assertThat(second).isSameAs(first)
    }

    @Test
    fun `등록하지 않은 인터페이스를 요청하면 오류가 발생한다`() {
        val container = DependencyContainer()

        assertThatThrownBy { container.get(UnregisteredDependency::class) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("주 생성자를 찾을 수 없습니다")
    }

    @Test
    fun `순환 의존성은 경로를 포함한 오류를 낸다`() {
        val container = DependencyContainer()

        assertThatThrownBy { container.get(CircularDependencyA::class) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("CircularDependencyA -> CircularDependencyB -> CircularDependencyA")
    }
}

class RegisteredDependency

class IntermediateDependency(
    val dependency: RegisteredDependency,
)

interface UnregisteredDependency

class CircularDependencyA(
    val dependency: CircularDependencyB,
)

class CircularDependencyB(
    val dependency: CircularDependencyA,
)
