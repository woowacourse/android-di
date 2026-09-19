@file:Suppress("NonAsciiCharacters")

package woowacourse.shopping.di

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertSame
import org.junit.Test

class DependencyContainerTest {
    class Child

    class Parent(
        val child: Child
    )

    @Test
    fun `Parent를 DependencyContainer에서 생성할 수 있다`() {
        val parent = DependencyContainer.create(Parent::class)

        assertEquals(parent::class.java, Parent::class.java)
    }

    @Test
    fun `Parent 안에 주입된 Child가 있다`() {
        val parent = DependencyContainer.create(Parent::class) as Parent

        assertEquals(parent.child::class.java, Child::class.java)
    }

    @Test
    fun `따로 getInstance(Child)를 호출했을 때 Parent 안의 Child와 같은 인스턴스이다`() {
        val parent = DependencyContainer.create(Parent::class) as Parent
        val child1 = parent.child
        val child2 = DependencyContainer.getInstance(Child::class)

        assertSame(child1, child2)
    }

    @Test
    fun `getInstance(Parent)를 두 번 호출했을 때 두 Parent는 같은 인스턴스이다`() {
        val parent1 = DependencyContainer.getInstance(Parent::class)
        val parent2 = DependencyContainer.getInstance(Parent::class)

        assertSame(parent1, parent2)
    }
}