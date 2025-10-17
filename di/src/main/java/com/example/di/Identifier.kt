package com.example.di

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.hasAnnotation

data class Identifier(
    val kType: KType,
    val qualifier: Annotation? = null,
    val lifespan: Annotation? = null,
) {
    companion object {
        fun from(property: KProperty1<*, *>): Identifier =
            Identifier(
                property.returnType,
                qualifier(property),
                lifespan(property),
            )

        fun from(parameter: KParameter): Identifier =
            Identifier(
                parameter.type,
                qualifier(parameter),
                lifespan(parameter),
            )

        private fun qualifier(element: KAnnotatedElement): Annotation? {
            val qualifiers: List<Annotation> =
                element.annotations.filter { annotation: Annotation ->
                    annotation.annotationClass.hasAnnotation<Qualifier>()
                }
            if (qualifiers.size > 1) error("$element has more than one qualifier: $qualifiers")
            return qualifiers.firstOrNull()
        }

        private fun lifespan(element: KAnnotatedElement): Annotation? {
            val lifespans: List<Annotation> =
                element.annotations.filter { annotation: Annotation ->
                    annotation.annotationClass.hasAnnotation<Lifespan>()
                }
            if (lifespans.size > 1) error("$element specifies more than one lifespan: $lifespans")
            return lifespans.firstOrNull()
        }
    }
}
