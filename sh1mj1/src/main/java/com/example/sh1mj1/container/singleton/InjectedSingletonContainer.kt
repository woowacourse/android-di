package com.example.sh1mj1.container.singleton

import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.component.singleton.ComponentKey
import com.example.sh1mj1.component.singleton.InjectedSingletonComponent
import kotlin.reflect.KClass

interface InjectedSingletonContainer {
    fun add(component: InjectedSingletonComponent)

    fun find(
        clazz: KClass<*>,
        qualifier: Qualifier?,
    ): Any?

    fun find(componentKey: ComponentKey): Any

    fun clear()
}
