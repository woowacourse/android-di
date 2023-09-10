package woowacourse.shopping.common

import kotlin.reflect.KClass

interface AppContainer {

    fun getInstance(type: KClass<*>): Any?

    fun addInstance(type: KClass<*>, instance: Any)

    fun clear()
}
