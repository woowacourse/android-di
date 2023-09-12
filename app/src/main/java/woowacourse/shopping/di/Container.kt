package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType

object Container {

    private val instances = mutableMapOf<KClass<*>, Any?>()

    fun addInstance(key: KClass<*>, instance: Any?) {
        instances[key] = instance
    }

    fun getInstance(type: KType): Any? {
        instances.forEach { instance ->
            val modelClass = instance.key
            if (modelClass.createType() == type) {
                return instance.value
            }
            if (modelClass.supertypes.any { it == type }) {
                return createInstance(instance.value, modelClass)
            }
        }
        return null
    }

    private fun createInstance(
        instanceValue: Any?,
        modelClass: KClass<*>,
    ): Any? {
        return if (instanceValue == null) {
            instances[modelClass] = Injector(this).createInstance(modelClass)
            instances[modelClass]
        } else {
            instanceValue
        }
    }
}
