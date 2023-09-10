package woowacourse.shopping.data.di

import kotlin.reflect.KClass

interface Container {
    val instances:MutableMap<KClass<*>, Any>

    fun addInstance(clazz: KClass<*>, instance:Any)

    fun getInstance(clazz: KClass<*>):Any?
}