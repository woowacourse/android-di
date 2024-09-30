package com.example.sh1mj1.component.activityscope

import android.app.Activity
import androidx.lifecycle.LifecycleOwner
import com.example.sh1mj1.DiApplication
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class ActivityScopeComponentFactory<T : Any>(
    private val injectedClass: KClass<T>,
) : ReadOnlyProperty<Activity, T> {
    private var cachedValue: T? = null

    override fun getValue(
        thisRef: Activity,
        property: KProperty<*>,
    ): T {
        cachedValue?.let { return it }

        val container = (thisRef.application as DiApplication).activityContainer
        val component =
            container.findComponent(injectedClass)
                ?: throw IllegalArgumentException("${injectedClass.simpleName} not found in container")

        val componentInstance =
            container.findComponentInstance(injectedClass, thisRef)
                ?: throw IllegalArgumentException("${injectedClass.simpleName} not found in container")

        if (thisRef is LifecycleOwner) {
            component.attachToLifecycle(thisRef)
        }

        cachedValue = componentInstance
        return componentInstance
    }
}

inline fun <reified T : Any> injectActivityScopeComponent() = ActivityScopeComponentFactory(T::class)
