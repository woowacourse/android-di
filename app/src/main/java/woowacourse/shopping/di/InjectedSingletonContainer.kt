package woowacourse.shopping.di

import kotlin.reflect.KClass

interface InjectedSingletonContainer {
    fun add(component: InjectedComponent.InjectedSingletonComponent)

    fun find(clazz: KClass<*>): Any?
}
