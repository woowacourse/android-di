package com.example.sh1mj1.component.activityscope

import android.content.Context
import com.example.sh1mj1.annotation.Qualifier
import kotlin.reflect.KClass

data class ActivityScopeComponent(
    val injectedClass: KClass<*>,
    val instanceProvider: (Context) -> Any,
    val qualifier: Qualifier? = null,
)

inline fun <reified T : Any> activityScopeComponent(
    noinline instanceProvider: (Context) -> T,
    qualifier: Qualifier? = null,
): ActivityScopeComponent =
    ActivityScopeComponent(
        injectedClass = T::class,
        instanceProvider = instanceProvider,
        qualifier = qualifier
    )
