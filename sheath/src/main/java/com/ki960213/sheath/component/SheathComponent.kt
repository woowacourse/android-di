package com.ki960213.sheath.component

import kotlin.reflect.KClass

abstract class SheathComponent {

    abstract val clazz: KClass<*>

    abstract val name: String

    abstract val isSingleton: Boolean

    abstract val dependentCount: Int

    abstract fun isDependingOn(component: SheathComponent): Boolean

    abstract fun instantiated(instances: List<Any>): Any

    override fun equals(other: Any?): Boolean =
        if (other is SheathComponent) clazz == other.clazz else false

    override fun hashCode(): Int = clazz.hashCode()

    override fun toString(): String = "SheathComponent(clazz=$clazz)"
}
