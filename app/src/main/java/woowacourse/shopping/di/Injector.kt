package woowacourse.shopping.di

import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

class Injector(private val container: Container) {

    inline fun <reified T : Any> getInstance(): T {
        val constructor = T::class.primaryConstructor ?: throw IllegalStateException()
        val parameters = constructor.parameters
        val arguments = parameters.map { getArgument(it.type) }
        val instance = constructor.call(*arguments.toTypedArray())
        injectField(instance)
        return instance
    }

    fun <T : Any> injectField(instance: T) {
        instance::class.memberProperties.forEach { property ->
            if (property.annotations.any { it is InjectField }) {
                if (property is KMutableProperty<*>) {
                    property.setter.call(instance, getArgument(property.returnType))
                }
            }
        }
    }

    fun getArgument(type: KType): Any {
        return container::class.declaredMemberProperties.forEach {
            if (type == it.returnType) {
                return it.getter.call(container) ?: throw java.lang.IllegalArgumentException()
            }
        }
    }
}
