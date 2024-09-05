package woowacourse.shopping.data.di

import kotlin.reflect.KClass

object Container {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun <T : Any> addInstance(
        classType: KClass<*>,
        instance: T,
    ) {
        instances[classType] = instance
    }

    fun <T : Any> instance(classType: KClass<*>): Any = instances[classType] as? T ?: throw IllegalArgumentException("Unknown Instance")
}
