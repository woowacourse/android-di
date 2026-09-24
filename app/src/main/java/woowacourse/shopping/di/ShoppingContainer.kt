package woowacourse.shopping.di

import kotlin.reflect.KClass

class ShoppingContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun getInstance(targetClass: KClass<*>): Any? = instances[targetClass]

    fun saveInstance(
        key: KClass<*>,
        value: Any,
    ) {
        instances[key] = value
    }
}
