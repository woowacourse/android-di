package com.ki960213.sheath.sorter

import com.ki960213.sheath.component.SheathComponent
import kotlin.properties.Delegates

class Node1(val sheathComponent: SheathComponent) {

    val dependentCount: Int = sheathComponent.dependentCount

    var inDegreeCount: Int by Delegates.observable(dependentCount) { _, _, newValue ->
        check(newValue >= 0) { "노드의 진입 차수가 0 미만일 수 없습니다." }
    }
        private set

    fun minusInDegree() {
        inDegreeCount--
    }

    fun isDependingOn(other: Node1): Boolean = sheathComponent.isDependingOn(other.sheathComponent)

    override fun equals(other: Any?): Boolean =
        if (other is Node1) sheathComponent == other.sheathComponent else false

    override fun hashCode(): Int = sheathComponent.hashCode()
}
