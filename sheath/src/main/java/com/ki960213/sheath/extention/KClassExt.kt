package com.ki960213.sheath.extention

import com.ki960213.sheath.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters

internal fun KClass<*>.getDependingTypes(): List<KType> = getConstructorInjectionDependingTypes() +
    getPropertyInjectionDependingTypes() +
    getFunctionInjectionDependingTypes()

private fun KClass<*>.getConstructorInjectionDependingTypes(): List<KType> =
    (constructors.find { it.hasAnnotation<Inject>() } ?: primaryConstructor)
        ?.valueParameters
        ?.map { it.type }
        ?: emptyList()

private fun KClass<*>.getPropertyInjectionDependingTypes(): List<KType> = declaredMemberProperties
    .filter { it.hasAnnotation<Inject>() }
    .map { it.returnType }

private fun KClass<*>.getFunctionInjectionDependingTypes(): List<KType> = declaredMemberFunctions
    .filter { it.hasAnnotation<Inject>() }
    .flatMap { func -> func.valueParameters.map { it.type } }
