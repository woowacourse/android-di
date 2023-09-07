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
        val node1 = Node(Test1::class)
        val node2 = Node(Test2::class)

        assertThat(node1.inDegreeCount)
            .isEqualTo(node1.clazz.primaryConstructor?.parameters?.size ?: 0)
        assertThat(node2.inDegreeCount)
            .isEqualTo(node2.clazz.primaryConstructor?.parameters?.size ?: 0)
    }

    @Test
    fun `노드의 클래스의 주생성자의 매개변수 개수와 의존 개수는 같다`() {
        val node1 = Node(Test1::class)
        val node2 = Node(Test2::class)

        expect.that(node1.dependentCount)
            .isEqualTo(Test1::class.primaryConstructor?.parameters?.size ?: 0)
        expect.that(node2.dependentCount)
            .isEqualTo(Test2::class.primaryConstructor?.parameters?.size ?: 0)
    }

    @Test
    fun `노드의 진입 차수를 마이너스 했을 때 음수가 되면 에러가 발생한다`() {
        val node = Node(Test1::class)

        try {
            node.minusInDegree()
        } catch (e: IllegalStateException) {
            assertThat(e).hasMessageThat().isEqualTo("노드의 진입 차수가 0 미만일 수 없습니다.")
        }
    }

    @Test
    fun `노드의 클래스의 주 생성자의 어떤 파라미터의 타입이 다른 노드의 클래스의 슈퍼 타입이라면 그 노드에 의존하는 것이다`() {
        val node1 = Node(Test1::class)
        val node2 = Node(Test3::class)

        assertTrue(node1.isDependingOn(node2))
    }

    @Test
    fun `노드의 클래스의 주 생성자의 모든 파라미터의 타입이 다른 노드의 클래스의 슈퍼 타입이 아니라면 그 노드에 의존하지 않는 것이다`() {
        val node1 = Node(Test1::class)
        val node2 = Node(Test3::class)

        assertFalse(node2.isDependingOn(node1))
    }

    @Test
    fun `노드의 클래스가 같다면 같다고 판단한다`() {
        val node1 = Node(Test1::class)
        val node2 = Node(Test1::class)

        assert(node1 == node2)
    }

    @Test
    fun `노드의 클래스가 다르면 다르다고 판단한다`() {
        val node1 = Node(Test1::class)
        val node2 = Node(Test2::class)

        assert(node1 != node2)
    }

    @Test
    fun `노드의 해시 코드는 노드가 소유한 클래스의 해시 코드와 같다`() {
        val node = Node(Test1::class)

        assert(node.hashCode() == Test1::class.hashCode())
    }

    @Test
    fun `노드를 문자열로 바꾸면 노드가 소유한 클래스의 이름으로 변한다`() {
        val node = Node(Test1::class)

        assert(node.toString() == Test1::class.toString())
    }

    private class Test1(val test2: Test2)

    private open class Test2

    private class Test3 : Test2()
}
