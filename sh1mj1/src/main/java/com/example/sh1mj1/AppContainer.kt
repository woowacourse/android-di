package com.example.sh1mj1

import kotlin.reflect.KClass

interface AppContainer {
    fun add(component: InjectedComponent)

    fun add(vararg component: InjectedComponent)

    fun find(
        clazz: KClass<*>,
        qualifier: Qualifier,
    ): Any?

    fun findWithKey(componentKey: ComponentKey): Any

    fun clearActivityScopedObjects()
}

inline fun <reified T> AppContainer.find(qualifier: Qualifier): T = find(T::class, qualifier) as T

data class ComponentKey(
    val clazz: KClass<*>,
    val qualifier: Qualifier? = null,
)
