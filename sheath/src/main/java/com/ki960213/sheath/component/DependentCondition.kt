package com.ki960213.sheath.component

import com.ki960213.sheath.annotation.NewInstance
import com.ki960213.sheath.annotation.Qualifier
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

data class DependentCondition(
    val isNewInstance: Boolean,
    val qualifier: KClass<*>? = null,
) {
    companion object {
        fun from(element: KAnnotatedElement): DependentCondition = DependentCondition(
            isNewInstance = element.hasAnnotation<NewInstance>(),
            qualifier = element.findAnnotation<Qualifier>()?.value,
        )
    }
}
