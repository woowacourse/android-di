package com.kmlibs.supplin.model

import android.content.Context
import kotlin.reflect.KClass

class InjectionData(
    val modules: List<KClass<*>>,
    val context: Context,
)
