package com.example.sh1mj1.container

import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.component.ComponentKey
import com.example.sh1mj1.component.InjectedComponent
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
