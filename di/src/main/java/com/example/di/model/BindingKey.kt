package com.example.di.model

import com.example.di.util.findSingleQualifierOrNull
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

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

        fun from(param: KParameter): BindingKey {
            val kClass = param.type.classifier as KClass<out Any>
            val qualifierAnn = findSingleQualifierOrNull(param.annotations)
            return BindingKey(kClass, qualifierAnn)
        }

        fun from(fn: KFunction<*>): BindingKey {
            val kClass = fn.returnType.classifier as KClass<out Any>
            val qualifierAnn = findSingleQualifierOrNull(fn.annotations)
            return BindingKey(kClass, qualifierAnn)
        }
    }
}
