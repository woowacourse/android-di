@file:Suppress("NonAsciiCharacters")

package woowacourse.shopping.di

import woowacourse.di.DependencyContainer
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue

class DependencyScopeTest {
    class ScopedDependency

    class ApplicationDependency

    @Test
    fun `같은 스코프에서는 같은 인스턴스를 재사용하고 다른 스코프에서는 새로 생성한다`() {
        val firstScope = DependencyContainer.openScope("scope-a")
        val secondScope = DependencyContainer.openScope("scope-b")

        val first = DependencyContainer.getInstance(ScopedDependency::class, scope = firstScope)
        val repeated = DependencyContainer.getInstance(ScopedDependency::class, scope = firstScope)
        val second = DependencyContainer.getInstance(ScopedDependency::class, scope = secondScope)

        assertSame(first, repeated)
        assertNotSame(first, second)

        firstScope.close()
        secondScope.close()
    }

    @Test
    fun `애플리케이션 스코프 인스턴스를 하위 스코프에서도 공유한다`() {
        val applicationInstance = DependencyContainer.getInstance(ApplicationDependency::class)
        val scope = DependencyContainer.openScope("application-fallback")

        val scopedInstance = DependencyContainer.getInstance(ApplicationDependency::class, scope = scope)

        assertSame(applicationInstance, scopedInstance)

        scope.close()
    }

    @Test
    fun `스코프를 닫으면 인스턴스를 정리하고 콜백을 한 번 호출한다`() {
        var closeCount = 0
        val scope = DependencyContainer.openScope("close-test") { closeCount++ }
        DependencyContainer.getInstance(ScopedDependency::class, scope = scope)

        scope.close()
        scope.close()

        assertTrue(scope.isClosed)
        assertTrue(scope.instances.isEmpty())
        assertTrue(closeCount == 1)
    }

    @Test
    fun `종료된 스코프 키를 다시 열면 새 스코프를 만든다`() {
        val first = DependencyContainer.openScope("reopen-test")
        first.close()

        val second = DependencyContainer.openScope("reopen-test")

        assertFalse(second.isClosed)
        assertNotSame(first, second)

        second.close()
    }
}
