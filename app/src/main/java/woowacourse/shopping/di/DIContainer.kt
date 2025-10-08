package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class DIContainer(private val interfaceMapping: Map<KClass<*>, KClass<*>>) {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun getInstance(kClass: KClass<*>): Any {
        interfaceMapping[kClass]?.let {
            return getInstance(it)
        }
        val existingInstance = instances[kClass]
        if (existingInstance != null) {
            return existingInstance
        }

        val createdInstance = createNewInstance(kClass)
        registerRepository(createdInstance)
        return createdInstance
    }

    private fun createNewInstance(kClass: KClass<*>): Any {
        val constructor = kClass.primaryConstructor ?: throw IllegalStateException()

        val parameterMap = constructor.parameters
            .filterNot { it.isOptional }
            .associateWith { param ->
                val paramType = param.type.classifier as KClass<*>
                getInstance(paramType)
            }
        return constructor.callBy(parameterMap)
    }

    private inline fun <reified T> registerRepository(instance: T) {
        instances[T::class] = instance as Any
    }
}
