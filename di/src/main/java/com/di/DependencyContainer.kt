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
        qualifier: String? = null
    ): T {
        val key = classType to qualifier
        return instances[key]?.let { instance ->
            instance as T
        } ?: throw IllegalArgumentException("Unknown Instance")
    }


    // Reflection을 사용하여 Inject 어노테이션이 붙은 프로퍼티에 의존성 주입
    // instance가 KClass인지 체크를 해야하나?
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
