package com.example.seogi.di.util

import com.example.seogi.di.Module
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions

internal fun Module.findDependencyFunctionsToRemove(
    annotation: Annotation,
    types: List<KType>,
): List<KFunction<*>> =
    this::class.declaredFunctions
        .filter { it.visibility == KVisibility.PUBLIC }
        .filter { it.annotations.contains(annotation) }
        .filter { types.contains(it.returnType) }
