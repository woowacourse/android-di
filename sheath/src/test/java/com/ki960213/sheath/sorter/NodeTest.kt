package com.ki960213.sheath.sorter

import com.google.common.truth.Expect
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.full.primaryConstructor

internal class NodeTest {

    @JvmField
    @Rule
    val expect: Expect = Expect.create()

    @Test
    fun `노드를 생성했을 때 진입 차수는 클래스의 주 생성자의 매개변수 개수와 같다`() {
        val node1 = Node(Test1::class.java)
        val node2 = Node(Test2::class.java)

        assertThat(node1.inDegreeCount).isEqualTo(node1.clazz.constructors.first().parameterCount)
        assertThat(node2.inDegreeCount).isEqualTo(node2.clazz.constructors.first().parameterCount)
    }

    @Test
    fun `노드의 클래스의 주생성자의 매개변수 개수와 의존 개수는 같다`() {
        val node1 = Node(Test1::class.java)
        val node2 = Node(Test2::class.java)

        expect.that(node1.dependentCount)
            .isEqualTo(Test1::class.primaryConstructor?.parameters?.size ?: 0)
        expect.that(node2.dependentCount)
            .isEqualTo(Test2::class.primaryConstructor?.parameters?.size ?: 0)
    }

    @Test
    fun `노드의 진입 차수를 마이너스 했을 때 음수가 되면 에러가 발생한다`() {
        val node = Node(Test1::class.java)

        try {
            node.minusInDegree()
        } catch (e: IllegalStateException) {
            assertThat(e).hasMessageThat().isEqualTo("노드의 진입 차수가 0 미만일 수 없습니다.")
        }
    }

    @Test
    fun `노드의 클래스의 주 생성자의 어떤 파라미터의 타입이 다른 노드의 클래스에 할당 가능하다면 종속적이다`() {
        val node1 = Node(Test1::class.java)
        val node2 = Node(Test2::class.java)

        assertTrue(node1.isDependingOn(node2))
    }

    @Test
    fun `노드의 클래스의 주 생성자의 모든 파라미터의 타입이 다른 노드의 클래스에 할당 가능하지 않다면 종속적이지 않다`() {
        val node1 = Node(Test1::class.java)
        val node2 = Node(Test2::class.java)

        assertFalse(node2.isDependingOn(node1))
    }

    @Test
    fun `노드의 클래스가 같다면 같다고 판단한다`() {
        val node1 = Node(Test1::class.java)
        val node2 = Node(Test1::class.java)

        assert(node1 == node2)
    }

    @Test
    fun `노드의 클래스가 다르면 다르다고 판단한다`() {
        val node1 = Node(Test1::class.java)
        val node2 = Node(Test2::class.java)

        assert(node1 != node2)
    }

    @Test
    fun `노드의 해시 코드는 노드가 소유한 클래스의 해시 코드와 같다`() {
        val node = Node(Test1::class.java)

        assert(node.hashCode() == Test1::class.java.hashCode())
    }

    @Test
    fun `노드를 문자열로 바꾸면 노드가 소유한 클래스의 이름으로 변한다`() {
        val node = Node(Test1::class.java)

        assert(node.toString() == Test1::class.java.name)
    }

    private class Test1(val test2: Test2)

    private class Test2
}
