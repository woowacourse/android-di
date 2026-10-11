package woowacourse.shopping.di

import junit.framework.TestCase.assertSame
import org.junit.Test

class DependencyContainerScopeTest {
    private class ScopeDependency

    @Test
    fun `자식 스코프는 앱 의존성을 상속하고 같은 타입은 자식 등록값을 우선한다`() {
        val applicationDependency = ScopeDependency()
        DependencyContainer.register(ScopeDependency::class, applicationDependency)

        val screenScope = DependencyContainer.createScope()

        assertSame(
            applicationDependency,
            DependencyContainer.get(ScopeDependency::class, scope = screenScope),
        )

        val screenDependency = ScopeDependency()
        DependencyContainer.register(
            ScopeDependency::class,
            screenDependency,
            scope = screenScope,
        )

        assertSame(
            screenDependency,
            DependencyContainer.get(ScopeDependency::class, scope = screenScope),
        )
        assertSame(applicationDependency, DependencyContainer.get(ScopeDependency::class))
    }

    @Test
    fun `임의 부모를 가진 스코프를 중첩할 수 있다`() {
        val destinationScope = DependencyContainer.createScope()
        val viewModelScope = DependencyContainer.createScope(destinationScope)
        val destinationDependency = ScopeDependency()
        DependencyContainer.register(
            ScopeDependency::class,
            destinationDependency,
            scope = destinationScope,
        )

        assertSame(
            destinationDependency,
            DependencyContainer.get(ScopeDependency::class, scope = viewModelScope),
        )

        viewModelScope.close()

        assertSame(
            destinationDependency,
            DependencyContainer.get(ScopeDependency::class, scope = destinationScope),
        )
    }
}
