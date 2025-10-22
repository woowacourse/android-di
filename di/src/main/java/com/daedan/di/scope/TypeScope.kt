package com.daedan.di.scope

import kotlin.reflect.KClass

data class TypeScope(
    val type: KClass<*>,
) : Scope
