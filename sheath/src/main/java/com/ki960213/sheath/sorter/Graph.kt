package com.ki960213.sheath.sorter

internal class Graph(nodes: Set<Node>) {

    private val dependencyMap: Map<Node, List<Node>> = nodes.associateWith { node ->
        val otherNodes = nodes.filterNot { it == node }
        val dependNodes = otherNodes.filter { it.isDependingOn(node) }
        dependNodes
    }

    init {
        nodes.forEach { node ->
            val otherNodes = nodes.filterNot { it == node }
            val dependentCountInGraph = node.getDependentCount(otherNodes)
            if (dependentCountInGraph < node.dependentCount) {
                throw IllegalArgumentException("${node.clazz} 클래스의 종속 항목 중 등록되지 않은 클래스가 있습니다.")
            }
            if (dependentCountInGraph > node.dependentCount) {
                throw IllegalArgumentException("${node.clazz} 클래스에 중복 종속 항목이 존재합니다.")
            }
        }
    }

    private fun Node.getDependentCount(otherNodes: List<Node>): Int =
        otherNodes.count { this.isDependingOn(it) }

    fun getNodesThatDependOn(node: Node): List<Node> =
        dependencyMap[node] ?: throw IllegalArgumentException("$node 노드는 그래프에 없는 노드입니다.")
}
