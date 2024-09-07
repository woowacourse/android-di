package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

object DIContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun getInstance(type: KClass<*>): Any = instances[type] ?: type.createInstance()

    fun setInstance(
        type: KClass<*>,
        instance: Any,
    ) {
        instances[type] = instance
    }
}
