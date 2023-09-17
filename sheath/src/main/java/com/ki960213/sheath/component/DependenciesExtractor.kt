package com.ki960213.sheath.component

import com.ki960213.sheath.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

object DependenciesExtractor {

    fun extractFrom(clazz: KClass<*>): Map<KType, DependentCondition> =
        (clazz.extractFromConstructor() + clazz.extractFromProperties() + clazz.extractFromFunctions())
            .takeUnless { it.isEmpty() }
            ?: clazz.extractFromPrimaryConstructor()
            ?: mapOf()

    fun extractFrom(function: KFunction<*>): Map<KType, DependentCondition> =
        function.valueParameters
            .associate { it.type to DependentCondition.from(it) }

    private fun KClass<*>.extractFromConstructor(): Map<KType, DependentCondition> =
        constructors.find { it.hasAnnotation<Inject>() }
            ?.valueParameters
            ?.associate { it.type to DependentCondition.from(it) }
            ?: mapOf()

    private fun KClass<*>.extractFromProperties(): Map<KType, DependentCondition> =
        declaredMemberProperties.filter { it.hasAnnotation<Inject>() }
            .associate { it.returnType to DependentCondition.from(it) }

    private fun KClass<*>.extractFromFunctions(): Map<KType, DependentCondition> =
        declaredMemberFunctions.filter { it.hasAnnotation<Inject>() }
            .flatMap { function ->
                function.valueParameters.map { it.type to DependentCondition.from(it) }
            }
            .toMap()

    private fun KClass<*>.extractFromPrimaryConstructor(): Map<KType, DependentCondition>? =
        primaryConstructor?.valueParameters
            ?.associate { it.type to DependentCondition.from(it) }
}
