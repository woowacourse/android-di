package com.example.sh1mj1

import kotlin.reflect.KClass

interface AppContainer {
    fun add(component: InjectedComponent)

    fun add(vararg component: InjectedComponent)

    fun find(
        clazz: KClass<*>,
        qualifier: Qualifier?,
    ): Any

    fun find(componentKey: ComponentKey): Any

    fun clearActivityScopedObjects()
}
