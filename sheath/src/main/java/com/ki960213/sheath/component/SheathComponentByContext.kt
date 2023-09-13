package com.ki960213.sheath.component

import android.content.Context
import kotlin.reflect.KClass

internal class SheathComponentByContext(private val context: Context) : SheathComponent() {
    override val clazz: KClass<*> = Context::class
    override val name: String = Context::class.qualifiedName!!
    override val isSingleton: Boolean = true
    override val dependentCount: Int = 0

    override fun isDependingOn(component: SheathComponent): Boolean = false

    override fun instantiated(instances: List<Any>): Any = context
}
