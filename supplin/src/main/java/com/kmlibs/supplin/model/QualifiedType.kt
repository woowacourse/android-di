package com.kmlibs.supplin.model

import kotlin.reflect.KType

data class QualifiedType(
    val returnType: KType,
    val qualifier: String? = null,
)
