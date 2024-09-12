package com.bignerdranch.android.haeum2

import kotlin.reflect.KType

data class Type(
    val returnType: KType,
    val qualifierName: String? = null,
)
