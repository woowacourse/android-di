package com.daedan.compactAndroidDi.qualifier

import kotlin.reflect.KClass

data class TypeQualifier(
    val type: KClass<*>,
) : Qualifier
