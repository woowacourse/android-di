package com.example.di

import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.hasAnnotation

data class Identifier(
    val tType: KType,
    val qualifier: Annotation?,
) {
    companion object {
        private fun qualifier(element: KAnnotatedElement): Annotation? {
            val qualifiers: List<Annotation> =
                element.annotations.filter { annotation: Annotation ->
                    annotation.annotationClass.hasAnnotation<Qualifier>()
                }
            if (qualifiers.size > 1) error("${element}이(가) 두 개 이상의 Qualifier를 사용하고 있습니다. ($qualifiers)")
            return qualifiers.firstOrNull()
        }

        fun of(property: KProperty1<*, *>): Identifier =
            Identifier(
                property.returnType,
                qualifier(property),
            )

        fun of(parameter: KParameter): Identifier =
            Identifier(
                parameter.type,
                qualifier(parameter),
            )
    }
}
