package com.example.sh1mj1.container

import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.component.InjectedComponent
import kotlin.reflect.KClass

interface InjectedActivityContainer {
    fun add(component: InjectedComponent.InjectedActivityComponent)

    fun find(clazz: KClass<*>): Any?

    fun find(
        clazz: KClass<*>,
        qualifier: Qualifier,
    ): Any?

    fun clear()
}
