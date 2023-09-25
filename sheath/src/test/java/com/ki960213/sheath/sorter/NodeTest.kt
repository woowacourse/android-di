package com.ki960213.sheath.sorter

import com.google.common.truth.Truth.assertThat
import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.component.ClassSheathComponent
import org.junit.Test

internal class NodeTest {

    @Test
    fun `노드의 의존 개수는 SheathComponent의 의존 개수와 같다`() {
        val node1 = Node(ClassSheathComponent(Test1::class))

        val actual = node1.dependentCount

        assertThat(actual).isEqualTo(1)
    }

    @Test
    fun `노드의 진입 차수를 마이너스 했을 때 음수가 되면 에러가 발생한다`() {
        val node = Node(ClassSheathComponent(Test1::class))

        try {
            node.minusInDegree()
        } catch (e: IllegalStateException) {
            assertThat(e).hasMessageThat().isEqualTo("노드의 진입 차수가 0 미만일 수 없습니다.")
        }
    }

    @Test
    fun `노드의 sheathComponent가 다른 노드의 sheathComponent에 의존한다면 그 노드는 다른 노드에 의존하는 것이다`() {
        val node1 = Node(ClassSheathComponent(Test1::class))
        val node2 = Node(ClassSheathComponent(Test2::class))

        val actual = node1.isDependingOn(node2)

        assertThat(actual).isTrue()
    }

    @Test
    fun `노드의 sheathComponent가 다른 노드의 sheathComponent에 의존하지 않는다면 그 노드는 다른 노드에 의존하지 않는 것이다`() {
        val node1 = Node(ClassSheathComponent(Test2::class))
        val node2 = Node(ClassSheathComponent(Test1::class))

        val actual = node1.isDependingOn(node2)

        assertThat(actual).isFalse()
    }

    @Component
    private class Test1(test2: Test2)

    @Component
    private class Test2

    @Test
    fun `노드는 sheathComponent가 같으면 같다고 판단한다`() {
        val node1 = Node(ClassSheathComponent(Test1::class))
        val node2 = Node(ClassSheathComponent(Test1::class))

        assertThat(node1).isEqualTo(node2)
    }

    @Test
    fun `노드는 sheathComponent가 다르면 다르다고 판단한다`() {
        val node1 = Node(ClassSheathComponent(Test1::class))
        val node2 = Node(ClassSheathComponent(Test2::class))

        assertThat(node1).isNotEqualTo(node2)
    }

    @Test
    fun `노드의 해시 코드는 sheathComponent의 해시 코드와 같다`() {
        val sheathComponent = ClassSheathComponent(Test1::class)
        val node = Node(sheathComponent)

        val actual = node.hashCode()

        assertThat(actual).isEqualTo(sheathComponent.hashCode())
    }
}
