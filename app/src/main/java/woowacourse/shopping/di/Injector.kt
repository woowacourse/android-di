package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

class Injector(private val container: Container) {

    fun inject(modelClass: KClass<*>): Any =
        getInstance(modelClass.createType()) ?: createInstance(modelClass)

    private fun getInstance(type: KType): Any? =
        container.getInstance(type)

    fun createInstance(kClass: KClass<*>): Any {
        val constructor = kClass.primaryConstructor ?: throw IllegalStateException()
        val parameters = constructor.parameters
        val instance = createInstanceWithArgument(parameters, kClass, constructor)
        injectField(instance)

        return instance
    }

    private fun createInstanceWithArgument(
        parameters: List<KParameter>,
        kClass: KClass<*>,
        constructor: KFunction<Any>,
    ): Any = if (parameters.isEmpty()) {
        kClass.createInstance()
    } else {
        val arguments = parameters.map { getInstance(it.type) }
        constructor.call(*arguments.toTypedArray())
    }

    private fun <T : Any> injectField(instance: T) {
        instance::class.memberProperties.forEach { property ->
            if (property.annotations.any { it is InjectField }) {
                println()
                if (property is KMutableProperty<*>) {
                    property.setter.call(instance, getInstance(property.returnType))
                }
            }
        }
    }
}
