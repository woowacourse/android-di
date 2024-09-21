package com.example.sh1mj1

import kotlin.reflect.KClass

interface InjectedSingletonContainer {
    fun add(component: InjectedComponent.InjectedSingletonComponent)

    fun find(clazz: KClass<*>): Any?

    fun find(
        clazz: KClass<*>,
        qualifier: Qualifier?,
    ): Any?

    fun findWithKey(componentKey: ComponentKey): Any
}
