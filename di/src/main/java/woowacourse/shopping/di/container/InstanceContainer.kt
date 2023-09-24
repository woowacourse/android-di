package woowacourse.shopping.di.container

import kotlin.reflect.KClass

interface InstanceContainer {
    val value: List<Any>

    fun add(instance: Any)

    fun find(clazz: Any): Any?

    fun remove(clazz: KClass<*>)

    fun clear()
}
