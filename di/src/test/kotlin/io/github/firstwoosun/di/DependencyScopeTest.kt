package io.github.firstwoosun.di

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class DependencyScopeTest {
    class AppDependency
    class VmDependency
    class ScreenDependency

    private fun container() =
        DependencyContainer(
            instanceProvider = InstanceProvider { null },
            bindings = listOf(
                DependencyBinding(
                    AppDependency::class,
                    AppDependency::class,
                    scopeKind = ScopeKind.Application,
                ),
                DependencyBinding(
                    VmDependency::class,
                    VmDependency::class,
                    scopeKind = ScopeKind.ViewModel,
                ),
                DependencyBinding(
                    ScreenDependency::class,
                    ScreenDependency::class,
                    scopeKind = ScopeKind.Screen,
                ),
            ),
        )

    @Test
    fun `앱 의존성은 서로 다른 자식 스코프에서 공유된다`() {
        val container = container()
        val first = container.applicationScope.openChild(ScopeKind.ViewModel)
        val second = container.applicationScope.openChild(ScopeKind.Screen)

        val a = container.getInstance(AppDependency::class, scope = first)
        val b = container.getInstance(AppDependency::class, scope = second)

        assertThat(a).isSameAs(b)

        container.close()
    }

    @Test
    fun `ViewModel 의존성은 같은 스코프에서 재사용하고 다른 스코프에서는 분리한다`() {
        val container = container()
        val first = container.applicationScope.openChild(ScopeKind.ViewModel)
        val second = container.applicationScope.openChild(ScopeKind.ViewModel)

        val a = container.getInstance(VmDependency::class, scope = first)
        val b = container.getInstance(VmDependency::class, scope = first)
        val c = container.getInstance(VmDependency::class, scope = second)

        assertThat(a).isSameAs(b)
        assertThat(a).isNotSameAs(c)

        container.close()
    }

    @Test
    fun `화면 의존성은 같은 화면에서 재사용하고 재진입하면 새로 생성한다`() {
        val container = container()
        val first = container.applicationScope.openChild(ScopeKind.Screen)

        val a = container.getInstance(ScreenDependency::class, scope = first)
        val b = container.getInstance(ScreenDependency::class, scope = first)

        assertThat(a).isSameAs(b)

        first.close()

        assertThat(first.instanceCount).isZero()
        assertThat(container.applicationScope.childCount).isZero()

        val second = container.applicationScope.openChild(ScopeKind.Screen)
        val c = container.getInstance(ScreenDependency::class, scope = second)

        assertThat(a).isNotSameAs(c)

        container.close()
    }

    @Test
    fun `종료한 스코프는 조회할 수 없다`() {
        val container = container()
        val scope = container.applicationScope.openChild(ScopeKind.ViewModel)
        scope.close()

        assertThatThrownBy {
            container.getInstance(VmDependency::class, scope = scope)
        }.isInstanceOf(IllegalStateException::class.java)

        container.close()
    }

    @Test
    fun `앱 종료는 앱과 모든 자식 스코프의 참조를 정리한다`() {
        val container = container()
        val vm = container.applicationScope.openChild(ScopeKind.ViewModel)
        val screen = container.applicationScope.openChild(ScopeKind.Screen)

        container.getInstance(AppDependency::class)
        container.getInstance(VmDependency::class, scope = vm)
        container.getInstance(ScreenDependency::class, scope = screen)

        container.close()

        assertThat(container.applicationScope.instanceCount).isZero()
        assertThat(container.applicationScope.childCount).isZero()
        assertThat(vm.instanceCount).isZero()
        assertThat(screen.instanceCount).isZero()
        assertThat(vm.isClosed).isTrue()
        assertThat(screen.isClosed).isTrue()
    }

    @Test
    fun `화면 진입과 종료를 반복해도 스코프가 누적되지 않는다`() {
        val container = container()

        repeat(20) {
            val scope = container.applicationScope.openChild(ScopeKind.Screen)
            container.getInstance(ScreenDependency::class, scope = scope)

            scope.close()

            assertThat(scope.instanceCount).isZero()
            assertThat(container.applicationScope.childCount).isZero()
        }

        container.close()
    }
}