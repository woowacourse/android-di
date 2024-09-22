package com.example.sh1mj1.component.activityscope

import android.app.Activity
import android.util.Log
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
        println("TAG, getValue: thisRef: $thisRef, property: $property app: ${thisRef.application}")
        val container = (thisRef.application as DiApplication).activityContainer
        return container.find(injectedClass, thisRef)
            ?: throw IllegalStateException("${injectedClass.simpleName} not found in container")
    }
}

// 커스텀 프로퍼티 위임 함수
inline fun <reified T : Any> injectActivityScopeComponent() = ActivityScopeComponentFactory(T::class)

private const val TAG = "ActivityScopeComponentF"