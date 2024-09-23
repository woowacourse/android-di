package com.kmlibs.supplin.model

import kotlin.reflect.KClass

data class QualifiedContainerType(
    val module: KClass<*>,
    val name: String?,
)
