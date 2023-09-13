package com.ki960213.sheath.sorter

internal class Graph(nodes: Set<Node>) {

    private val dependencyMap: Map<Node, List<Node>> = nodes.associateWith { node ->
        val otherNodes = nodes.filterNot { it == node }
        otherNodes.filter { it.isDependingOn(node) }
    }

    init {
        validateNodes(nodes)
    }

    fun getNodesThatDependOn(node: Node): List<Node> =
        dependencyMap[node] ?: throw IllegalArgumentException("$node 노드는 그래프에 없는 노드입니다.")

    private fun validateNodes(nodes: Set<Node>) {
        nodes.forEach { node ->
            val otherNodes = nodes.filterNot { it == node }
            val dependentCountInGraph = node.getDependentCount(otherNodes)
            if (dependentCountInGraph < node.dependentCount) {
                throw IllegalArgumentException("${node.sheathComponent} 컴포넌트의 종속 항목 중 등록되지 않은 컴포넌트가 있습니다.")
            }
            if (dependentCountInGraph > node.dependentCount) {
                throw IllegalArgumentException("${node.sheathComponent} 컴포넌트에 모호한 종속 항목이 존재합니다.")
            }
        }
    }

    private fun Node.getDependentCount(otherNodes: List<Node>): Int =
        otherNodes.count { this.isDependingOn(it) }
}
