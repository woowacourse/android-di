package com.example.di

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.hasAnnotation

data class Identifier(
    val kType: KType,
    val qualifier: Annotation?,
) {
    companion object {
        fun from(property: KProperty1<*, *>): Identifier =
            Identifier(
                property.returnType,
                qualifier(property),
            )

        fun from(parameter: KParameter): Identifier =
            Identifier(
                parameter.type,
                qualifier(parameter),
            )

        private fun qualifier(element: KAnnotatedElement): Annotation? {
            val qualifiers: List<Annotation> =
                element.annotations.filter { annotation: Annotation ->
                    annotation.annotationClass.hasAnnotation<Qualifier>()
                }
            if (qualifiers.size > 1) error("$element has more than one qualifier: $qualifiers")
            return qualifiers.firstOrNull()
        }
    }
}
