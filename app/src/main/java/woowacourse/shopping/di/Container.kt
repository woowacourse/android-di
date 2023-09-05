package woowacourse.shopping.di

import kotlin.reflect.KClass

interface Container {
    fun getInstance(type: KClass<*>): Any?
    fun setInstance(type: KClass<*>, instance: Any)
}
