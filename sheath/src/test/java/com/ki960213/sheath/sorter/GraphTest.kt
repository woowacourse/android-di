package com.ki960213.sheath.sorter

import com.google.common.truth.Expect
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

internal class GraphTest {

    @JvmField
    @Rule
    val expect: Expect = Expect.create()

    @Test
    fun `그래프를 노드를 이용해 생성하면 각 노드를 의존하는 노드들을 저장한다`() {
        val node1 = Node(Test1::class.java)
        val node2 = Node(Test2::class.java)
        val node3 = Node(Test3::class.java)

        val graph = Graph(setOf(node1, node2, node3))

        expect.that(graph.getNodesThatDependOn(node1)).containsExactly(node3)
        expect.that(graph.getNodesThatDependOn(node2)).containsExactly(node1, node3)
        expect.that(graph.getNodesThatDependOn(node3)).containsExactly()
    }

    @Test
    fun `그래프를 노드를 이용해 생성하며 각 노드를 의존하는 노드의 개수만큼 노드의 진입 차수가 증가한다`() {
        val node1 = Node(Test1::class.java)
        val node2 = Node(Test2::class.java)
        val node3 = Node(Test3::class.java)

        val graph = Graph(setOf(node1, node2, node3))

        expect.that(node1.inDegreeCount).isEqualTo(1)
        expect.that(node2.inDegreeCount).isEqualTo(2)
        expect.that(node3.inDegreeCount).isEqualTo(0)
    }

    @Test
    fun `그래프에 없는 노드의 의존 노드를 가져오면 에러가 발생한다`() {
        val node1 = Node(Test1::class.java)
        val node2 = Node(Test2::class.java)
        val graph = Graph(setOf(node1))

        try {
            graph.getNodesThatDependOn(node2)
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageThat().isEqualTo("$node2 노드는 그래프에 없는 노드입니다.")
        }
    }

    private class Test1(val test2: Test2)

    private class Test2

    private class Test3(val test1: Test1, val test2: Test2)
}
