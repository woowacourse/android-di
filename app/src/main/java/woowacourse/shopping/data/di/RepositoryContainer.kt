package woowacourse.shopping.data.di

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class RepositoryContainer{
    private val repositoryStore = mutableMapOf<KClass<*>, Any>()

    fun inject(target: Any) {
        target::class.java.declaredFields
            .filter { field ->
                field.isAnnotationPresent(CustomFieldInjection::class.java)
            }
            .forEach { field ->
                val dependencyType = field.type.kotlin
                val dependency = getInstance(dependencyType)

                field.isAccessible = true
                field.set(target, dependency)
            }
    }

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
