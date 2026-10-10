package woowacourse.shopping.di

import org.junit.After
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class DIContainerScopeTest {
    private object TestScope : ScopeType

    private val firstScopeKey = ScopeKey(TestScope, "first")
    private val secondScopeKey = ScopeKey(TestScope, "second")
    private val dependencyKey = DependencyKey(TestDependency::class)

    @Before
    fun setUp() {
        DIContainer.clear()
    }

    @After
    fun tearDown() {
        DIContainer.clear()
    }

    @Test
    fun `같은 스코프에서는 인스턴스를 재사용한다`() {
        DIContainer.openScope(firstScopeKey)

        val first = DIContainer.createInstance(dependencyKey, firstScopeKey)
        val second = DIContainer.createInstance(dependencyKey, firstScopeKey)

        assertSame(first, second)
    }

    @Test
    fun `다른 스코프에서는 다른 인스턴스를 생성한다`() {
        DIContainer.openScope(firstScopeKey)
        DIContainer.openScope(secondScopeKey)

        val first = DIContainer.createInstance(dependencyKey, firstScopeKey)
        val second = DIContainer.createInstance(dependencyKey, secondScopeKey)

        assertNotSame(first, second)
    }

    @Test
    fun `스코프를 닫고 다시 열면 새 인스턴스를 생성한다`() {
        DIContainer.openScope(firstScopeKey)
        val first = DIContainer.createInstance(dependencyKey, firstScopeKey)

        DIContainer.closeScope(firstScopeKey)
        assertThrows(IllegalStateException::class.java) {
            DIContainer.createInstance(dependencyKey, firstScopeKey)
        }

        DIContainer.openScope(firstScopeKey)
        val second = DIContainer.createInstance(dependencyKey, firstScopeKey)

        assertNotSame(first, second)
    }

    class TestDependency
}
