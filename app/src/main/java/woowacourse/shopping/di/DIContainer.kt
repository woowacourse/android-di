package woowacourse.shopping.di

import kotlin.reflect.KClass

class DIContainer {
    private val classes: MutableMap<KClass<*>, Any> = mutableMapOf()

    fun get(classType: KClass<*>, field: List<Any> = emptyList()): Any {
        val instance = classes[classType]
        instance ?: return create(classType, field)
        return instance
    }

    private fun create(classType: KClass<*>, field: List<Any>): Any {
        val instance = classType.constructors.first().call(*field.toTypedArray())
        classes[classType] = instance
        return instance
    }
}
