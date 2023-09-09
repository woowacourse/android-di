package woowacourse.shopping.di

import kotlin.reflect.KClass

object Container {
    private val repositories = mutableMapOf<KClass<*>, Any>()

    fun getInstance(type: KClass<*>): Any? {
        return repositories[type]
    }

    fun addInstance(type: KClass<*>, instance: Any) {
        repositories[type] = instance
    }

    fun clear() {
        repositories.clear()
    }
}
