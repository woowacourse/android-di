package com.daedan.di.qualifier

import kotlin.reflect.KClass

data class TypeQualifier(
    val type: KClass<*>,
) : Qualifier
