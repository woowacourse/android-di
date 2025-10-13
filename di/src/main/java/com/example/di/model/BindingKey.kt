package com.example.di.model

import kotlin.reflect.KClass

/**
 * 바인딩을 식별하는 키: (타입 + 선택적 Qualifier)
 */
data class BindingKey(
    val type: KClass<out Any>,
    val qualifier: KClass<out Annotation>? = null,
) {
    companion object {
        fun from(
            type: KClass<out Any>,
            qualifierAnn: Annotation?,
        ): BindingKey {
            val q = qualifierAnn?.annotationClass
            return BindingKey(type, q)
        }
    }
}
