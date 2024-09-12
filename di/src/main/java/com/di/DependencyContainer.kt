package woowacourse.shopping.data.di

import woowacourse.shopping.data.di.annotation.Inject
import woowacourse.shopping.data.di.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

object DependencyContainer {
    private val instances = mutableMapOf<Pair<KClass<*>, String?>, Any>()

    fun <T : Any> addInstance(
        classType: KClass<*>,
        instance: T,
        qualifier: String? = null,
    ) {
        val key = classType to qualifier
        if (instances.containsKey(key)) return
        instances[key] = instance
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> instance(
        classType: KClass<*>,
        qualifier: String? = null,
    ): T {
        val key = classType to qualifier
        return instances[key]?.let { instance ->
            instance as T
        } ?: throw IllegalArgumentException("Unknown Instance")
    }

    fun <T : Any> injectProperty(instance: T) {
        instance::class.declaredMemberProperties.forEach { property ->
            if (property.hasAnnotation<Inject>() && property is KMutableProperty<*>) {
                val qualifier = property.findAnnotation<Qualifier>()?.value
                val dependencyClass = instance(property.returnType.jvmErasure, qualifier) as Any
                property.isAccessible = true
                property.setter.call(instance, dependencyClass)
            }
        }
    }
}
