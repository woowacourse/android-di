@file:Suppress("NonAsciiCharacters")

package woowacourse.shopping.di

import woowacourse.di.DependencyContainer
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class DependencyContainerTest {
    private val testScopeId = 1L

    class Child

    class Parent(
        val child: Child,
    )

    @BeforeTest
    fun openScope() {
        DependencyContainer.openScope(testScopeId)
    }

    @AfterTest
    fun closeScope() {
        DependencyContainer.closeScope(testScopeId)
    }

    @Test
    fun `Parent를 DependencyContainer에서 생성할 수 있다`() {
        val parent = DependencyContainer.create(Parent::class, testScopeId)

        assertEquals(parent::class.java, Parent::class.java)
    }

    @Test
    fun `Parent 안에 주입된 Child가 있다`() {
        val parent = DependencyContainer.create(Parent::class, testScopeId) as Parent

        assertEquals(parent.child::class.java, Child::class.java)
    }

    @Test
    fun `따로 getInstance(Child)를 호출했을 때 Parent 안의 Child와 같은 인스턴스이다`() {
        val parent = DependencyContainer.create(Parent::class, testScopeId) as Parent
        val child1 = parent.child
        val child2 = DependencyContainer.getInstance(Child::class, testScopeId)

        assertSame(child1, child2)
    }

    @Test
    fun `getInstance(Parent)를 두 번 호출했을 때 두 Parent는 같은 인스턴스이다`() {
        val parent1 = DependencyContainer.getInstance(Parent::class, testScopeId)
        val parent2 = DependencyContainer.getInstance(Parent::class, testScopeId)

        assertSame(parent1, parent2)
    }
}
