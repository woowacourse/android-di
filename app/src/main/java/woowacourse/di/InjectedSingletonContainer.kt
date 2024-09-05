package woowacourse.di

import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

object InjectedSingletonContainer {
    private val components: MutableList<InjectedComponent.InjectedSingletonComponent> = mutableListOf()

    fun add(component: InjectedComponent.InjectedSingletonComponent) {
        components.add(component)
    }

    fun find(clazz: KClass<*>): Any? = components.find {
        clazz.isSuperclassOf(it.injectedClass)
    }?.instance
}