package com.example.sh1mj1.component

import android.app.Activity
import com.example.sh1mj1.DiApplication
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass

class InjectedActivityComponent<T : Any>(
    private val injectedClass: KClass<T>,
) : ReadOnlyProperty<Activity, T> {
    override fun getValue(
        thisRef: Activity,
        property: kotlin.reflect.KProperty<*>,
    ): T {
        val container = (thisRef.application as DiApplication).activityContainer
        return container.find(injectedClass, thisRef)
            ?: throw IllegalStateException("${injectedClass.simpleName} not found in container")
    }
}

// 커스텀 프로퍼티 위임 함수
inline fun <reified T : Any> injectedSh1mj1ActivityComponent() = InjectedActivityComponent(T::class)
