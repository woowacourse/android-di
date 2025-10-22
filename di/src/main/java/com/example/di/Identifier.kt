package com.example.di

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation

data class Identifier(
    val kClass: KClass<*>,
    val qualifier: Annotation? = null,
) {
    companion object {
        fun from(kClass: KClass<*>): Identifier =
            Identifier(
                kClass,
                qualifier(kClass),
            )

        fun from(property: KProperty1<*, *>): Identifier =
            Identifier(
                property.returnType.classifier as KClass<*>,
                qualifier(property),
            )

        fun from(function: KFunction<*>): Identifier =
            Identifier(
                function.returnType.classifier as KClass<*>,
                qualifier(function),
            )

        fun from(parameter: KParameter): Identifier =
            Identifier(
                parameter.type.classifier as KClass<*>,
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
