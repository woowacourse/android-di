package com.example.di.util

import com.example.di.annotation.Qualifier
import com.example.di.model.BindingKey
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

/**
 * 파라미터/프로퍼티에서 Qualifier 어노테이션(커스텀) 0~1개만 허용
 */
fun findSingleQualifierOrNull(annotations: List<Annotation>): KClass<out Annotation>? {
    val qualifiers =
        annotations
            .filter { it.annotationClass.findAnnotation<Qualifier>() != null }
    require(qualifiers.size <= 1) { "Qualifier는 최대 1개만 허용됩니다. annotations=$annotations" }
    return qualifiers.firstOrNull()?.annotationClass
}

fun KParameter.toBindingKey(): BindingKey {
    val kClass =
        requireNotNull(this.type.classifier as? KClass<out Any>) {
            "타입 정보를 알 수 없습니다: $this"
        }
    val q = findSingleQualifierOrNull(this.annotations)
    return BindingKey(kClass, q)
}
