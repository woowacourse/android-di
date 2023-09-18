package com.ki960213.sheath.extention

import android.content.ComponentCallbacks
import com.ki960213.sheath.SheathApplication
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSupertypeOf

inline fun <reified T : Any> ComponentCallbacks.inject(
    mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
) = lazy(mode) { get<T>() }

inline fun <reified T : Any> ComponentCallbacks.get(): T {
    val container = SheathApplication.sheathComponentContainer
    val sheathComponent = container[T::class.createType()]
        ?: container.values.find { T::class.createType().isSupertypeOf(it.type) }
        ?: throw IllegalArgumentException("${T::class.qualifiedName} 클래스가 컴포넌트로 등록되지 않았습니다.")

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
        ?: container.values.find { T::class.createType().isSupertypeOf(it.type) }
        ?: throw IllegalArgumentException("${T::class.qualifiedName} 클래스가 컴포넌트로 등록되지 않았습니다.")

    return sheathComponent.getNewInstance() as T
}
