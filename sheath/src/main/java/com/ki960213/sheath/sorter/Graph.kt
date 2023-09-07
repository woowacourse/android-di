package com.ki960213.sheath.sorter

internal class Graph(nodes: Set<Node>) {

    private val dependencyMap: Map<Node, List<Node>> = nodes.associateWith { node ->
        val otherNodes = nodes.filterNot { it == node }
        otherNodes.filter { it.isDependentOn(node) }
    }

    fun getNodesThatDependOn(node: Node): List<Node> =
        dependencyMap[node] ?: throw IllegalArgumentException("$node 노드는 그래프에 없는 노드입니다.")
}
