package woowacourse.shopping.di

import kotlin.reflect.KClass

interface InjectedActivityContainer {
    fun add(component: InjectedComponent.InjectedActivityComponent)

    fun find(clazz: KClass<*>): Any?

    fun clear()
}
