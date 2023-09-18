package com.ki960213.sheath.extention

import android.content.ComponentCallbacks
import com.ki960213.sheath.SheathApplication
import kotlin.reflect.full.createType

inline fun <reified T : Any> ComponentCallbacks.inject(
    mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
) = lazy(mode) { get<T>() }

inline fun <reified T : Any> ComponentCallbacks.get(): T {
    val container = SheathApplication.sheathComponentContainer
    val sheathComponent = container[T::class.createType()]

    val instance =
        if (sheathComponent.isSingleton) sheathComponent.instance else sheathComponent.getNewInstance()

    return instance as T
}

inline fun <reified T : Any> ComponentCallbacks.injectNewInstance(
    mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
) = lazy(mode) { create<T>() }

inline fun <reified T : Any> ComponentCallbacks.create(): T {
    val container = SheathApplication.sheathComponentContainer
    val sheathComponent = container[T::class.createType()]
    return sheathComponent.getNewInstance() as T
}
