package com.bignerdranch.android.koala

import kotlin.reflect.KType

object Container {

    val instances = mutableMapOf<KType, Any?>()
}
