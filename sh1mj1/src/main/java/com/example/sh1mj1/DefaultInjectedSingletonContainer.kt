package com.example.sh1mj1

import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

object DefaultInjectedSingletonContainer : InjectedSingletonContainer {
    private val components: MutableList<InjectedComponent.InjectedSingletonComponent> = mutableListOf()

    override fun add(component: InjectedComponent.InjectedSingletonComponent) {
        components.add(component)
    }

    override fun find(clazz: KClass<*>): Any? =
        components.find {
            clazz.isSuperclassOf(it.injectedClass)
        }?.instance

    override fun find(
        clazz: KClass<*>,
        qualifier: Qualifier,
    ): Any? = components.find {
        clazz.isSuperclassOf(it.injectedClass) && qualifier.value == it.qualifier?.value
    }?.instance
}

private const val TAG = "DefaultInjectedSingleto"
