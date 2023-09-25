package com.ki960213.sheath.component

import kotlin.reflect.KType
import kotlin.reflect.full.isSupertypeOf
import kotlin.reflect.jvm.jvmErasure

abstract class SheathComponent {
    abstract val type: KType

    abstract val name: String

    abstract val isSingleton: Boolean

    protected abstract val dependentConditions: Map<KType, DependentCondition>

    val dependentCount: Int
        get() = dependentConditions.size

    lateinit var instance: Any
        protected set

    fun isDependingOn(component: SheathComponent): Boolean {
        val type = dependentConditions.keys
            .find { it.isSupertypeOf(component.type) }
            ?: return false

        val qualifier = dependentConditions[type]!!.qualifier ?: return true

        return qualifier == component.type.jvmErasure
    }

    abstract fun instantiate(components: List<SheathComponent>)

    abstract fun getNewInstance(): Any

    override fun equals(other: Any?): Boolean =
        if (other is SheathComponent) type == other.type else false

    override fun hashCode(): Int = type.hashCode()

    override fun toString(): String = "SheathComponent(type=$type)"
}
