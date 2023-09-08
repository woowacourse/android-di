package woowacourse.shopping.di

import kotlin.reflect.KClass

data class InstanceInfo(val clazz: KClass<*>, val annotations: List<Annotation>)

object Container {
    private val instances = mutableMapOf<InstanceInfo, Any>()
    fun addInstance(type: KClass<*>, instance: Any) {
        val key = InstanceInfo(type, instance::class.annotations)
        instances[key] = instance
    }

    fun getInstance(type: KClass<*>, annotations: List<Annotation> = emptyList()): Any? {
        val key = InstanceInfo(type, annotations)
        return instances[key]
    }

    fun clear() {
        instances.clear()
    }
}
