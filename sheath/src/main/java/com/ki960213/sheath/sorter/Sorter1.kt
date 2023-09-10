package com.ki960213.sheath.sorter

import com.ki960213.sheath.component.SheathComponent
import java.util.LinkedList
import java.util.Queue

fun List<SheathComponent>.sorted(): List<SheathComponent> {
    val nodes: Set<Node1> = this.map(::Node1).toSet()
    val graph = Graph1(nodes)

    val result: MutableList<Node1> = mutableListOf()
    val queue: Queue<Node1> = LinkedList()
    queue.addAll(nodes.filter { it.inDegreeCount == 0 })

    repeat(nodes.size) {
        val node = queue.poll() ?: throw IllegalStateException("SheathComponent 간 의존 사이클이 존재합니다.")
        result.add(node)
        val dependNodes = graph.getNodesThatDependOn(node)
        queue.minusInDegreeAndAddNotDependentNodes(dependNodes)
    }

    return result.map(Node1::sheathComponent)
}

private fun Queue<Node1>.minusInDegreeAndAddNotDependentNodes(nodes: List<Node1>) {
    nodes.forEach {
        it.minusInDegree()
        if (it.inDegreeCount == 0) this.add(it)
    }
}
