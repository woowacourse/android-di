package com.ki960213.sheath.component

import android.content.Context
import kotlin.reflect.KType
import kotlin.reflect.full.createType

internal class ContextSheathComponent(
    private val context: Context,
) : SheathComponent() {
    override val type: KType = Context::class.createType()

    override val name: String = Context::class.qualifiedName!!

    override val isSingleton: Boolean = true

    override val dependentConditions: Map<KType, DependentCondition> = emptyMap()

    override fun instantiate(components: List<SheathComponent>) {
        instance = context
    }

    override fun getNewInstance(): Any = context
}
