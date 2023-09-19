package woowacourse.shopping.di.sgDi

import woowacourse.shopping.di.annotation.WooWaField
import woowacourse.shopping.di.annotation.WooWaQualifier
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

object DependencyInjector {

    inline fun <reified T : Any> inject(): T {
        val clazz = T::class
        val constructor =
            clazz.primaryConstructor
                ?: throw IllegalArgumentException("[ERROR] DependencyInjector:inject:constructor")
        val instances: MutableList<Any> = mutableListOf()

        constructor.parameters.forEach { parameter ->
            val instance = when (parameter.hasAnnotation<WooWaQualifier>()) {
                true -> getQualifiedInstance(parameter)
                false -> getInstance(parameter)
            }
            instances.add(instance)
        }

        return constructor.call(*instances.toTypedArray()).apply {
            injectField<T>(this)
        }
    }

    inline fun <reified T : Any> injectField(clazz: T) {
        val properties = T::class.declaredMemberProperties.filter { property ->
            property.hasAnnotation<WooWaField>()
        }

        if (properties.isEmpty()) return

        properties.forEach { property ->
            val instance = when (property.hasAnnotation<WooWaQualifier>()) {
                true -> getQualifiedInstance(property)
                false -> getInstance(property)
            }
            property.isAccessible = true
            property.javaField?.set(clazz, instance)
        }
    }

    fun getQualifiedInstance(argument: Any): Any {
        val type = when (argument) {
            is KParameter -> argument.findAnnotation<WooWaQualifier>()?.type
            is KProperty1<*, *> -> argument.findAnnotation<WooWaQualifier>()?.type
            else -> throw IllegalArgumentException("[ERROR] DependencyInjector:getQualifiedInstance:type")
        }

        return DependencyContainer.qualifiedRepository[type]
            ?: throw IllegalArgumentException("[ERROR] DependencyInjector:getQualifiedInstance")
    }

    fun getInstance(argument: Any): Any {
        val type = when (argument) {
            is KParameter -> argument.type.jvmErasure
            is KProperty1<*, *> -> argument.returnType.jvmErasure
            else -> throw IllegalArgumentException("[ERROR] DependencyInjector:getInstance:type")
        }

        return DependencyContainer.repositories[type]
            ?: throw IllegalArgumentException("[ERROR] DependencyInjector:getInstance")
    }
}
