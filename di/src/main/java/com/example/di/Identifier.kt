package com.example.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1

data class Identifier(
    val kClass: KClass<*>,
    val qualifier: Annotation? = null,
) {
    companion object {
        fun from(function: KFunction<*>): Identifier = Identifier(function.returnType.classifier as KClass<*>, Qualifier.from(function))

        fun from(property: KProperty1<*, *>): Identifier = Identifier(property.returnType.classifier as KClass<*>, Qualifier.from(property))

        fun from(parameter: KParameter): Identifier = Identifier(parameter.type.classifier as KClass<*>, Qualifier.from(parameter))
    }
}
