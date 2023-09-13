package com.ki960213.sheath.component

import com.ki960213.sheath.annotation.Prototype
import com.ki960213.sheath.extention.customQualifiedName
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.hasAnnotation

internal data class DependingCondition(
    val isSingleton: Boolean = true,
    val qualifiedName: String? = null,
) {
    companion object {
        fun from(element: KAnnotatedElement): DependingCondition =
            DependingCondition(element.hasAnnotation<Prototype>(), element.customQualifiedName)
    }
}
