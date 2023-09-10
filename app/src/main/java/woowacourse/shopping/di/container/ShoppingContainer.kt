package woowacourse.shopping.di.container

import kotlin.reflect.KClass

interface ShoppingContainer {
    fun <T : Any> createInstance(clazz: KClass<*>, instance: T)
    fun <T : Any> getInstance(clazz: KClass<T>): T?
}
