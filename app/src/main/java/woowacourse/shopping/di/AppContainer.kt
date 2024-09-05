package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class AppContainer {
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()

    fun getInstance(type: KClass<*>): Any {
        return instances[type] ?: type.createInstance()
    }

    fun addInstance(
        type: KClass<*>,
        instance: Any,
    ) {
        instances[type] = instance
    }
}
