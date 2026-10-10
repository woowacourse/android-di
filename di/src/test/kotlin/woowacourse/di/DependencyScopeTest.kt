package woowacourse.di

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class DependencyScopeTest {
    class Leaf

    class Branch(
        val leaf: Leaf,
    )

    class Target(
        val branch: Branch,
    ) {
        @KirbyInject
        lateinit var leaf: Leaf
    }

    @Test
    fun `컨테이너는 같은 스코프에서 객체를 재사용하고 다른 스코프와 분리한다`() {
        val container = KirbyDIContainer()
        val firstScope = container.createScope()
        val secondScope = container.createScope()

        val first = container.resolve(Leaf::class, scope = firstScope)

        assertThat(container.resolve(Leaf::class, scope = firstScope)).isSameAs(first)
        assertThat(container.resolve(Leaf::class, scope = secondScope)).isNotSameAs(first)
        assertThat(container.resolve(Leaf::class)).isNotSameAs(first)
        val otherContainer = KirbyDIContainer()
        assertThatThrownBy { otherContainer.resolve(Leaf::class, scope = firstScope) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("다른 컨테이너")
        assertThatThrownBy { otherContainer.createInstance(Target::class, firstScope) }
            .hasMessageContaining("다른 컨테이너")
    }

    @Test
    fun `컨테이너는 생성자와 필드의 재귀 주입에도 같은 스코프를 적용한다`() {
        val container = KirbyDIContainer()
        val scope = container.createScope()

        val first = container.createInstance(Target::class, scope)
        val second = container.createInstance(Target::class, scope)
        val other = container.createInstance(Target::class, container.createScope())

        assertThat(first).isNotSameAs(second)
        assertThat(first.branch).isSameAs(second.branch)
        assertThat(first.leaf).isSameAs(first.branch.leaf)
        assertThat(second.leaf).isSameAs(first.leaf)
        assertThat(other.branch).isNotSameAs(first.branch)
        assertThat(other.leaf).isNotSameAs(first.leaf)
    }

    @Test
    fun `스코프는 종료 시 참조를 제거하고 컨테이너는 종료된 스코프 사용을 거부한다`() {
        val container = KirbyDIContainer()
        val scope = container.createScope()
        val target = container.createInstance(Target::class, scope)
        val leafKey = DependencyKey(Leaf::class, null)
        val branchKey = DependencyKey(Branch::class, null)
        assertThat(scope.instanceStore.get(leafKey)).isSameAs(target.leaf)
        assertThat(scope.instanceStore.get(branchKey)).isSameAs(target.branch)

        scope.close()

        assertThat(scope.isClosed).isTrue()
        assertThat(scope.instanceStore.get(leafKey)).isNull()
        assertThat(scope.instanceStore.get(branchKey)).isNull()
        assertThatThrownBy { container.resolve(Leaf::class, scope = scope) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("종료된 스코프")
        assertThatThrownBy { container.createInstance(Target::class, scope) }
            .hasMessageContaining("종료된 스코프")
        scope.close()
        container.createScope().close()
    }

    @Test
    fun `한 스코프의 종료는 다른 스코프와 컨테이너 공유 객체에 영향을 주지 않는다`() {
        val container = KirbyDIContainer()
        container.registerBinding(Branch::class, Branch::class, lifetime = DependencyLifetime.CONTAINER)
        container.registerBinding(Leaf::class, Leaf::class, lifetime = DependencyLifetime.CONTAINER)
        val firstScope = container.createScope()
        val secondScope = container.createScope()
        val firstTarget = container.resolve(Target::class, scope = firstScope)
        val secondTarget = container.resolve(Target::class, scope = secondScope)

        firstScope.close()

        assertThat(container.resolve(Target::class, scope = secondScope)).isSameAs(secondTarget)
        assertThat(container.resolve(Branch::class, scope = secondScope)).isSameAs(firstTarget.branch)
        assertThat(container.resolve(Branch::class)).isSameAs(firstTarget.branch)
        assertThat(firstScope.instanceStore.get(DependencyKey(Target::class, null))).isNull()
    }
}
