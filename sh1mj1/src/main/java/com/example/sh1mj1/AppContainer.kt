package com.example.sh1mj1

interface AppContainer {
    fun add(component: InjectedComponent)

    fun add(vararg component: InjectedComponent)

    fun find(componentKey: ComponentKey): Any

    fun clearActivityScopedObjects()
}
