package woowacourse.shopping.di.container

import kotlin.reflect.KClass

interface ShoppingContainer {
    fun <T : Any> createInstance(clazz: KClass<T>, instance: T)
    fun <T : Any> getInstance(clazz: KClass<T>): T?
    fun clearInstance()
}
