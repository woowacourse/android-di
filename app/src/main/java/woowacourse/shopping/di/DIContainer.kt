package woowacourse.shopping.di

import kotlin.reflect.KClass

object DIContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun getInstance(type: KClass<*>): Any = instances[type] ?: throw NullPointerException("No Instance Found")

    fun setInstance(
        type: KClass<*>,
        instance: Any,
    ) {
        instances[type] = instance
    }
}
