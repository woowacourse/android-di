package woowacourse.shopping.data.di

import woowacourse.shopping.data.di.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

object DependencyContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun <T : Any> addInstance(
        classType: KClass<*>,
        instance: T,
    ) {
        if (instances.containsKey(classType)) return
        instances[classType] = instance
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> instance(classType: KClass<*>): T = instances[classType]?.let { instance ->
        instance as T
    } ?: throw IllegalArgumentException("Unknown Instance")


    // Reflection을 사용하여 Inject 어노테이션이 붙은 프로퍼티에 의존성 주입
    // instance가 KClass인지 체크를 해야하나?
    fun <T : Any> injectProperty(instance: T) {
        instance::class.declaredMemberProperties.forEach { property ->
            if (property.hasAnnotation<Inject>() && property is KMutableProperty<*>) {
                val dependencyClass = instance(property.returnType.jvmErasure) as Any
                property.isAccessible = true
                property.setter.call(instance, dependencyClass)
            }
        }
    }
}
