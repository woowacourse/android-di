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
    fun `그래프를 만들 때 어떤 노드가 노드 목록에 없는 노드를 의존한다면 에러가 발생한다`() {
        val nodes = setOf(
            Node(Test1::class.java),
            Node(Test2::class.java),
            Node(Test3::class.java),
            Node(Test4::class.java),
        )

        try {
            Graph(nodes)
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageThat()
                .isEqualTo("${Test4::class} 클래스의 종속 항목 중 등록되지 않은 클래스가 있습니다.")
        }
    }

    @Test
    fun `그래프를 만들 때 어떤 노드의 중복 종속 항목이 존재한다면 에러가 발생한다`() {
        val nodes = setOf(
            Node(Test7::class.java),
            Node(Test8::class.java),
            Node(Test9::class.java),
        )

        try {
            Graph(nodes)
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageThat()
                .isEqualTo("${Test9::class} 클래스에 중복 종속 항목이 존재합니다.")
        }
    }

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
    fun `그래프에 없는 노드의 의존 노드를 가져오면 에러가 발생한다`() {
        val node1 = Node(Test1::class.java)
        val node2 = Node(Test2::class.java)
        val node3 = Node(Test3::class.java)
        val graph = Graph(setOf(node1, node2))

        try {
            graph.getNodesThatDependOn(node3)
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageThat().isEqualTo("$node3 노드는 그래프에 없는 노드입니다.")
        }
    }

    private class Test1(val test2: Test2)

    private class Test2

    private class Test3(val test1: Test1, val test2: Test2)

    // 그래프에 없는 노드를 의존할 때 테스트 용
    private class Test4(val test5: Test5)
    private class Test5

    // 중복 종속 항목 테스트 용
    private open class Test6
    private class Test7 : Test6()
    private class Test8 : Test6()
    private class Test9(val test6: Test6)
}
