package com.example.sh1mj1

import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

object DefaultInjectedActivityContainer : InjectedActivityContainer {
    private val components: MutableList<InjectedComponent.InjectedActivityComponent> = mutableListOf()

    override fun add(component: InjectedComponent.InjectedActivityComponent) {
        components.add(component)
    }

    override fun find(clazz: KClass<*>): Any? =
        components.find {
            clazz.isSuperclassOf(it.injectedClass)
        }?.instance

    override fun find(
        clazz: KClass<*>,
        qualifier: Qualifier,
    ): Any? =
        components.find {
            clazz.isSuperclassOf(it.injectedClass) &&
                it::class.annotations.find { it.annotationClass == qualifier.annotationClass } != null
        }?.instance?.let {
            DefaultInjectedSingletonContainer.find(clazz)
        }

    override fun clear() {
        components.clear()
    }
}