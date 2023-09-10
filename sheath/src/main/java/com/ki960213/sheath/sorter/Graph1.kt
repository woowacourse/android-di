package com.ki960213.sheath.sorter

class Graph1(nodes: Set<Node1>) {

    private val dependencyMap: Map<Node1, List<Node1>> = nodes.associateWith { node ->
        val otherNodes = nodes.filterNot { it == node }
        otherNodes.filter { it.isDependingOn(node) }
    }

    init {
        validateNodes(nodes)
    }

    private fun validateNodes(nodes: Set<Node1>) {
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

    private fun Node1.getDependentCount(otherNodes: List<Node1>): Int =
        otherNodes.count { this.isDependingOn(it) }
}
