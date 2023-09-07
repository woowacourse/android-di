package com.ki960213.sheath.sorter

import java.util.LinkedList
import java.util.Queue
import kotlin.reflect.KClass

fun List<KClass<*>>.sortedTopologically(): List<KClass<*>> {
    val nodes = this.map(::Node).toSet()
    val graph = Graph(nodes)

    val result: MutableList<Node> = mutableListOf()
    val queue: Queue<Node> = LinkedList()
    queue.addAll(nodes.filter { it.inDegreeCount == 0 })

    repeat(nodes.size) {
        val node = queue.poll() ?: throw IllegalStateException("클래스 간 의존 사이클이 존재합니다.")
        result.add(node)
        val dependNodes = graph.getNodesThatDependOn(node)
        queue.minusInDegreeAndAddNotDependentNodes(dependNodes)
    }

    return result.map(Node::clazz)
}

private fun Queue<Node>.minusInDegreeAndAddNotDependentNodes(nodes: List<Node>) {
    nodes.forEach {
        it.minusInDegree()
        if (it.inDegreeCount == 0) this.add(it)
    }
}
