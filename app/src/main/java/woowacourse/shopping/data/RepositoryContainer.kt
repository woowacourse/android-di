package woowacourse.shopping.data

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class RepositoryContainer{
    private val repositoryStore = mutableMapOf<KClass<*>, Any>()

    fun getInstance(type: KClass<*>): Any {
        val storedInstance = findInstance(type)

        if(storedInstance != null) return storedInstance

        val newInstance = createInstance(type)
        storeInstance(type, newInstance)

        return newInstance
    }

    private fun findInstance(type: KClass<*>): Any? =
        repositoryStore[type]

    private fun createInstance(type: KClass<*>): Any =
        type.createInstance()

    private fun storeInstance(
        type: KClass<*>,
        instance: Any
    ) {
        repositoryStore[type] = instance
    }
}