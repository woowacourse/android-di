package woowacourse.di

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class DependencyLifetimeTest {
    class ContainerDependency

    class ScopedDependency

    class Consumer(
        val shared: ContainerDependency,
        val scoped: ScopedDependency,
    ) {
        @KirbyInject
        lateinit var sharedField: ContainerDependency

        @KirbyInject
        lateinit var scopedField: ScopedDependency
    }

    class InvalidContainerConstructor(
        val dependency: ScopedDependency,
    )

    class InvalidContainerField {
        @KirbyInject
        lateinit var dependency: ScopedDependency
    }

    @Test
    fun `컨테이너는 CONTAINER 객체를 여러 스코프에서 공유한다`() {
        val container = KirbyDIContainer()
        container.registerBinding(ContainerDependency::class, ContainerDependency::class, lifetime = DependencyLifetime.CONTAINER)
        container.registerBinding(ScopedDependency::class, ScopedDependency::class, lifetime = DependencyLifetime.EACH_SCOPE)
        val firstScope = container.createScope()
        val secondScope = container.createScope()

        val first = container.createInstance(Consumer::class, firstScope)
        val repeated = container.createInstance(Consumer::class, firstScope)
        val second = container.createInstance(Consumer::class, secondScope)

        assertThat(first.shared).isSameAs(second.shared)
        assertThat(first.sharedField).isSameAs(first.shared)
        assertThat(second.sharedField).isSameAs(first.shared)
        assertThat(first.scoped).isSameAs(repeated.scoped)
        assertThat(first.scopedField).isSameAs(first.scoped)
        assertThat(second.scopedField).isSameAs(second.scoped)
        assertThat(second.scoped).isNotSameAs(first.scoped)
        assertThat(container.resolve(ContainerDependency::class)).isSameAs(first.shared)
        val otherContainer = KirbyDIContainer()
        otherContainer.registerBinding(ContainerDependency::class, ContainerDependency::class, lifetime = DependencyLifetime.CONTAINER)
        assertThat(otherContainer.resolve(ContainerDependency::class)).isNotSameAs(first.shared)
    }

    @Test
    fun `컨테이너는 CONTAINER 객체의 EACH_SCOPE 의존성 참조를 거부한다`() {
        val container = KirbyDIContainer()
        container.registerBinding(
            InvalidContainerConstructor::class,
            InvalidContainerConstructor::class,
            lifetime = DependencyLifetime.CONTAINER,
        )
        val scope = container.createScope()

        assertThatThrownBy { container.resolve(InvalidContainerConstructor::class, scope = scope) }
            .hasMessageContaining("컨테이너 공유 의존성은 스코프 의존성을 참조할 수 없습니다")

        container.resolve(ScopedDependency::class, scope = scope)

        assertThatThrownBy { container.resolve(InvalidContainerConstructor::class, scope = scope) }
            .hasMessageContaining("컨테이너 공유 의존성은 스코프 의존성을 참조할 수 없습니다")
        container.registerBinding(InvalidContainerField::class, InvalidContainerField::class, lifetime = DependencyLifetime.CONTAINER)
        assertThatThrownBy { container.resolve(InvalidContainerField::class, scope = scope) }
            .hasMessageContaining("컨테이너 공유 의존성은 스코프 의존성을 참조할 수 없습니다")
    }
}
