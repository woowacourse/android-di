@file:Suppress("NonAsciiCharacters")

package woowacourse.shopping.di

import woowacourse.di.DependencyContainer
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
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

    @Test
    fun `자식 스코프에서 부모에 등록된 인스턴스를 재사용한다`() {
        val sharedChild = Child()
        DependencyContainer.register(testScopeId, Child::class, sharedChild)

        val childScopeId = DependencyContainer.openChildScope(testScopeId)

        try {
            val child = DependencyContainer.getInstance(Child::class, childScopeId)

            assertSame(sharedChild, child)
        } finally {
            DependencyContainer.closeScope(childScopeId)
        }
    }

    @Test
    fun `같은 스코프에서는 인스턴스를 재사용하고 형제 스코프에서는 따로 생성한다`() {
        val firstScopeId = DependencyContainer.openChildScope(testScopeId)
        val secondScopeId = DependencyContainer.openChildScope(testScopeId)

        try {
            val firstInstance = DependencyContainer.getInstance(Child::class, firstScopeId)
            val sameScopeInstance = DependencyContainer.getInstance(Child::class, firstScopeId)
            val secondInstance = DependencyContainer.getInstance(Child::class, secondScopeId)

            assertSame(firstInstance, sameScopeInstance)
            assertNotSame(firstInstance, secondInstance)
        } finally {
            DependencyContainer.closeScope(firstScopeId)
            DependencyContainer.closeScope(secondScopeId)
        }
    }

    @Test
    fun `스코프를 닫고 다시 열면 새 인스턴스를 생성한다`() {
        val childScopeId = DependencyContainer.openChildScope(testScopeId)

        try {
            val oldInstance = DependencyContainer.getInstance(Child::class, childScopeId)

            DependencyContainer.closeScope(childScopeId)
            DependencyContainer.openScope(childScopeId, parentId = testScopeId)

            val newInstance = DependencyContainer.getInstance(Child::class, childScopeId)

            assertNotSame(oldInstance, newInstance)
        } finally {
            DependencyContainer.closeScope(childScopeId)
        }
    }
}
