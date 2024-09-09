package woowacourse.shopping.data.di

import kotlin.reflect.KClass

object DependencyContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun <T : Any> addInstance(
        classType: KClass<*>,
        instance: T,
    ) {
        if (instances.containsKey(classType)) return
        instances[classType] = instance
    }

    fun <T : Any> instance(classType: KClass<*>): Any =
        instances[classType] as? T ?: throw IllegalArgumentException("Unknown Instance")
}
