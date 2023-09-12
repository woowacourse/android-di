package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KType

interface Container {

    fun addInstance(key: KClass<*>, instance: Any?)

    fun getInstance(type: KType): Any?
}
