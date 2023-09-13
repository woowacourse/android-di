package woowacourse.shopping.di

import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.di.model.InstanceIdentifier
import kotlin.reflect.KClass

object Container {
    private val instances = mutableMapOf<InstanceIdentifier, Any>()

    fun addInstance(type: KClass<*>, instance: Any) {
        val key = InstanceIdentifier(type, instance::class.annotations)
        instances[key] = instance
    }

    fun getInstance(clazz: KClass<*>, annotations: List<Annotation>): Any? {
        val key = InstanceIdentifier(clazz, annotations.filter { it !is Inject })
        return instances[key]
    }
}
