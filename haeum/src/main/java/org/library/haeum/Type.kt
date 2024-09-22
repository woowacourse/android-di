package org.library.haeum

import kotlin.reflect.KType

data class Type(
    val returnType: KType,
    val qualifierName: String? = null,
)
