package com.ki960213.sheath.extention

import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.valueParameters

internal fun KFunction<*>.getDependingTypes(): List<KType> = valueParameters.map { it.type }
