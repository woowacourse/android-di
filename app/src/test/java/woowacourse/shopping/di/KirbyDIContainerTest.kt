package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class KirbyDIContainerTest {
    class LeafDependency

    class OneDependency(
        val dependency: LeafDependency,
    )

    interface SampleRepository

    class RealSampleRepository(
        val dependency: LeafDependency,
    ) : SampleRepository

    class FieldTarget {
        @KirbyInject
        lateinit var injected: LeafDependency

        var notInjected: LeafDependency? = null
    }

    class CyclicA(
        val b: CyclicB,
    )

    class CyclicB(
        val a: CyclicA,
    )

    @Test
    fun `애노테이션이 붙은 필드만 주입되고 붙지 않은 필드는 주입되지 않는다`() {
        val container = KirbyDIContainer()

        val target = container.createInstance(FieldTarget::class)

        assertThat(target.injected).isNotNull()
        assertThat(target.notInjected).isNull()
    }

    @Test
    fun `생성자 파라미터의 의존성까지 재귀적으로 해결한다`() {
        val container = KirbyDIContainer()

        val instance = container.createInstance(OneDependency::class)

        assertThat(instance.dependency).isNotNull()
    }

    @Test
    fun `인터페이스를 요청하면 바인딩된 구현체로 생성한다`() {
        val container = KirbyDIContainer()
        container.registerBinding(SampleRepository::class, RealSampleRepository::class)

        val repository = container.resolve(SampleRepository::class)

        assertThat(repository).isInstanceOf(RealSampleRepository::class.java)
    }

    @Test
    fun `의존성은 공유하고 최상위 인스턴스는 요청할 때마다 새로 만든다`() {
        val container = KirbyDIContainer()

        val first = container.createInstance(OneDependency::class)
        val second = container.createInstance(OneDependency::class)

        assertThat(first).isNotSameAs(second)
        assertThat(first.dependency).isSameAs(second.dependency)
    }

    @Test
    fun `순환 의존성이면 예외를 던진다`() {
        val container = KirbyDIContainer()

        assertThatThrownBy { container.createInstance(CyclicA::class) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }
}
