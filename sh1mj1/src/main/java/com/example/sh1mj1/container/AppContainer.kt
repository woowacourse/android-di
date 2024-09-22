package com.example.sh1mj1.container

import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.component.singleton.ComponentKey
import com.example.sh1mj1.component.singleton.InjectedSingletonComponent
import kotlin.reflect.KClass

interface AppContainer {
    fun <T : Any> add(component: InjectedSingletonComponent<T>)

    fun add(vararg component: InjectedSingletonComponent<*>)

    fun<T : Any> find(
        clazz: KClass<T>,
        qualifier: Qualifier?,
    ): Any

    fun find(componentKey: ComponentKey): Any

    fun clearActivityScopedObjects()
}
