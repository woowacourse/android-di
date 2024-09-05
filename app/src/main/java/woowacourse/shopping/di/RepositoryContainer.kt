package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

object RepositoryContainer {
    private val repositories = mutableMapOf<KClass<*>, Any>()

    fun getInstance(type: KClass<*>): Any = repositories[type] ?: type.createInstance()

    fun setInstance(
        type: KClass<*>,
        instance: Any,
    ) {
        repositories[type] = instance
    }
}
