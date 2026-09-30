package woowacourse.shopping.di

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Test

class DependencyScopeTest {
    @Test
    fun `자식 스코프는 부모 의존성을 찾고 같은 키는 자신의 의존성을 우선한다`() {
        val applicationScope = DependencyScope()
        val screenScope = DependencyScope(parent = applicationScope)

        val repository = Any()
        val applicationFormatter = Any()
        val screenFormatter = Any()

        applicationScope.put("repository", repository)
        applicationScope.put("formatter", applicationFormatter)
        screenScope.put("formatter", screenFormatter)

        assertSame(repository, screenScope.find("repository"))
        assertSame(screenFormatter, screenScope.find("formatter"))
        assertSame(applicationFormatter, applicationScope.find("formatter"))
    }

    @Test
    fun `스코프를 비우면 자식 의존성만 제거하고 부모 의존성은 유지한다`() {
        val applicationScope = DependencyScope()
        val screenScope = DependencyScope(parent = applicationScope)

        val applicationFormatter = Any()
        val screenFormatter = Any()
        val screenOnlyFormatter = Any()

        applicationScope.put("formatter", applicationFormatter)
        screenScope.put("formatter", screenFormatter)
        screenScope.put("screenOnly", screenOnlyFormatter)

        screenScope.clear()

        assertSame(applicationFormatter, screenScope.find("formatter"))
        assertNull(screenScope.find("screenOnly"))
        assertSame(applicationFormatter, applicationScope.find("formatter"))
    }

    @Test
    fun `자식 스코프의 키에는 부모 스코프의 키도 포함된다`() {
        val applicationScope = DependencyScope()
        val screenScope = DependencyScope(parent = applicationScope)

        applicationScope.put("repository", Any())
        screenScope.put("formatter", Any())

        assertEquals(setOf("repository", "formatter"), screenScope.keys())
    }
}
