package com.example.sh1mj1.component.activityscope

import android.app.Activity
import com.example.sh1mj1.DiApplication
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class ActivityScopeComponentFactory<T : Any>(
    private val injectedClass: KClass<T>,
) : ReadOnlyProperty<Activity, T> {
    override fun getValue(
        thisRef: Activity,
        property: KProperty<*>,
    ): T {
        val container = (thisRef.application as DiApplication).activityContainer
        return container.find(injectedClass, thisRef)
            ?: throw IllegalStateException("${injectedClass.simpleName} not found in container")
    }
}

inline fun <reified T : Any> injectActivityScopeComponent() = ActivityScopeComponentFactory(T::class)

private const val TAG = "ActivityScopeComponentF"
