package com.ki960213.sheath.component

import android.content.Context
import kotlin.reflect.KType

internal class ContextSheathComponent(
    type: KType,
    name: String,
    isSingleton: Boolean,
    dependentConditions: Map<KType, DependentCondition>,
    private val context: Context,
) : SheathComponent(
    type = type,
    name = name,
    isSingleton = isSingleton,
    dependentConditions = dependentConditions,
) {
    override fun instantiate(components: List<SheathComponent>) {
        instance = context
    }

    override fun getNewInstance(): Any = context
}
