package com.example.sh1mj1.component.activityscope

import android.app.Activity
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
        println("$TAG thisRef: $thisRef") // 이거 왜 출력 안댐

        val value = container.find(injectedClass, thisRef)
            ?: throw IllegalStateException("${injectedClass.simpleName} not found in container")

        cachedValue = value
        return value
    }
}

inline fun <reified T : Any> injectActivityScopeComponent() = ActivityScopeComponentFactory(T::class)

private const val TAG = "ActivityScopeComponentFactory"
