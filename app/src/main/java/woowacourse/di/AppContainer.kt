package woowacourse.di

import kotlin.reflect.KClass

interface AppContainer {
    fun add(component: InjectedComponent)

    fun add(vararg component: InjectedComponent)

    fun find(clazz: KClass<*>): Any?

    fun clearActivityScopedObjects()
}
