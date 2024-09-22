package com.example.sh1mj1.component.activityscope

import android.content.Context
import com.example.sh1mj1.annotation.Qualifier
import kotlin.reflect.KClass

data class ActivityComponent(
    val injectedClass: KClass<*>,
    val instanceProvider: (Context) -> Any,
    val qualifier: Qualifier? = null,
)
