package com.ki960213.sheath.sorter

import java.lang.reflect.Constructor
import kotlin.properties.Delegates.observable

internal class Node(val clazz: Class<*>) {

    val dependentCount = clazz.primaryConstructor?.parameterCount ?: 0

    var inDegreeCount: Int by observable(dependentCount) { _, _, newValue ->
        check(newValue >= 0) { "노드의 진입 차수가 0 미만일 수 없습니다." }
    }
        private set

    fun minusInDegree() {
        inDegreeCount--
    }

    fun isDependingOn(other: Node): Boolean {
        val constructor = this.clazz.primaryConstructor ?: return false
        return constructor.parameters.any { it.type.isAssignableFrom(other.clazz) }
    }

    private val Class<*>.primaryConstructor: Constructor<*>?
        get() = this.declaredConstructors.firstOrNull()

    override fun equals(other: Any?): Boolean = if (other is Node) clazz == other.clazz else false

    override fun hashCode(): Int = clazz.hashCode()

    override fun toString(): String = clazz.name
}
