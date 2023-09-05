package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

object RepositoryContainer {
    private val repositories = mutableMapOf<KClass<*>, Any>()

    fun getInstance(type: KClass<*>): Any {
        return repositories[type] ?: type.createInstance()
    }

    fun addInstance(type: KClass<*>, instance: Any) {
        repositories[type] = instance
    }

    fun clear() {
        repositories.clear()
    }
}
