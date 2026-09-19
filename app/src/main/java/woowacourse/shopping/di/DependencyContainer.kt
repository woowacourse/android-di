package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DependencyContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun create(type: KClass<*>): Any {
        val objectInstance = type.objectInstance
        if (objectInstance != null) return objectInstance

        val constructor = type.primaryConstructor
            ?: throw IllegalArgumentException()
        val parameters = constructor.parameters
        val args = parameters.map { parameter ->
            val dependencyClass =
                parameter.type.classifier as? KClass<*>
                    ?: throw IllegalArgumentException()

            getInstance(dependencyClass)
        }
        return constructor.call(*args.toTypedArray())
    }

    fun getInstance(type: KClass<*>): Any {
        val objectInstance = type.objectInstance
        if (objectInstance != null) return objectInstance

        return instances.getOrPut(type) {
            create(type)
        }
    }
}
