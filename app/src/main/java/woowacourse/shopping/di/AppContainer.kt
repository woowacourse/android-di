package woowacourse.shopping.di

import kotlin.reflect.KClass

interface AppContainer {
    fun add(component: InjectedComponent)

    fun add(vararg component: InjectedComponent)

    fun find(clazz: KClass<*>): Any?

    fun find(
        clazz: KClass<*>,
        qualifier: Qualifier,
    ): Any?

    fun clearActivityScopedObjects()
}
