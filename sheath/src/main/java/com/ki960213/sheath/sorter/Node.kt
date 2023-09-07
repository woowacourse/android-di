package com.ki960213.sheath.sorter

import kotlin.properties.Delegates.observable
import kotlin.reflect.KClass
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.full.primaryConstructor

internal class Node(val clazz: KClass<*>) {

    val dependentCount = clazz.primaryConstructor?.parameters?.size ?: 0

    var inDegreeCount: Int by observable(dependentCount) { _, _, newValue ->
        check(newValue >= 0) { "노드의 진입 차수가 0 미만일 수 없습니다." }
    }
        private set

    fun minusInDegree() {
        inDegreeCount--
    }

    fun isDependingOn(other: Node): Boolean {
        val constructor = this.clazz.primaryConstructor ?: return false
        return constructor.parameters.any { it.type.isSupertypeOf(other.clazz.createType()) }
    }

    override fun equals(other: Any?): Boolean = if (other is Node) clazz == other.clazz else false

    override fun hashCode(): Int = clazz.hashCode()

    override fun toString(): String = clazz.toString()
}
