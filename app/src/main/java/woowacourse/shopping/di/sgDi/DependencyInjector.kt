package woowacourse.shopping.di.sgDi

import woowacourse.shopping.util.annotation.WooWaField
import woowacourse.shopping.util.annotation.WooWaInject
import woowacourse.shopping.util.annotation.WooWaLazyInject
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

object DependencyInjector {

    inline fun <reified T : Any> inject(): T {
        val clazz = T::class
        val constructor = clazz.primaryConstructor ?: throw IllegalArgumentException()
        val arguments: MutableList<Any> = mutableListOf()

        when {
            constructor.hasAnnotation<WooWaInject>() -> {
                constructor.parameters.forEach { parameter ->
                    val type: KClass<*> = parameter.type.jvmErasure
                    val argument =
                        DependencyContainer.repositories[type] ?: throw IllegalArgumentException()
                    arguments.add(argument)
                }
            }

            clazz.hasAnnotation<WooWaLazyInject>() -> {
                // 지연초기화 대응
            }
        }

        return constructor.call(*arguments.toTypedArray()).apply {
            injectField<T>(this)
        }
    }

    inline fun <reified T : Any> injectField(clazz: T) {
        val properties = T::class.declaredMemberProperties.filter { property ->
            property.hasAnnotation<WooWaField>()
        }
        if (properties.isEmpty()) return
        properties.forEach { property ->
            val type = property.returnType.jvmErasure
            val instance =
                DependencyContainer.repositories[type] ?: throw IllegalArgumentException()
            property.isAccessible = true
            property.javaField?.set(clazz, instance)
        }
    }
}
