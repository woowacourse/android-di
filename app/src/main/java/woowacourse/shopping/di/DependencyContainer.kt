package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

object DependencyContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun getInstance(type: KClass<*>): Any {
        val objectInstance = type.objectInstance
        if (objectInstance != null) return objectInstance
        return instances.getOrPut(type) {
            type.createInstance()
        }
    }
}
