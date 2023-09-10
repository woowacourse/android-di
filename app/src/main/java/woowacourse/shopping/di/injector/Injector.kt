package woowacourse.shopping.di.injector

import woowacourse.shopping.di.annotation.Injected
import woowacourse.shopping.di.container.ShoppingContainer
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.jvmName

class Injector(
    private val container: ShoppingContainer,
) {
    fun <T : Any> create(clazz: KClass<T>): T {
        val instance = container.getInstance(clazz)

        if (instance != null) return instance
        return createInstance(clazz)
    }

    private fun <T> createInstance(clazz: KClass<*>): T {
        val constructor = clazz.primaryConstructor
            ?: throw IllegalArgumentException("${clazz.jvmName} $ERROR_NO_CONSTRUCTOR")
        val arguments = getArguments(constructor)
        val instance = constructor.callBy(arguments) as T

        injectOnFields(clazz, instance)
//        container.createInstance(clazz, instance as Any)

        return instance
    }

    private fun getArguments(constructor: KFunction<*>): Map<KParameter, Any?> {
        val parameters = constructor.parameters
            .filter { it.hasAnnotation<Injected>() }

        return parameters.associateWith { parameter ->
            val type = parameter.type.jvmErasure
            container.getInstance(type)
                ?: container.createInstance(type, create(type))
        }
    }

    private fun <T> injectOnFields(clazz: KClass<*>, instance: T) {
        val properties = clazz.declaredMemberProperties
            .filter { it.hasAnnotation<Injected>() }
            .filterIsInstance<KMutableProperty<*>>()

        properties.forEach { property ->
            property.isAccessible = true
            property.setter.call(instance, container.getInstance(property.returnType.jvmErasure))
        }
    }

    companion object {
        private const val ERROR_NO_CONSTRUCTOR = "주생성자가 존재하지 않습니다"
        private const val ERROR_NO_FIELD = "컨테이너에 해당 인스턴스가 존재하지 않습니다"
    }
}
