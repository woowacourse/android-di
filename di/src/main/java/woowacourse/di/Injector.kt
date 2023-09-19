package woowacourse.di

import woowacourse.di.annotation.InjectField
import woowacourse.di.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class Injector(private val container: Container) {

    fun <T : Any> inject(modelClass: KClass<T>): T {
        val result = container.getInstance(modelClass) ?: createInstance(modelClass)
        return result as T
    }

    private fun <T : Any> createInstance(modelClass: KClass<*>): T {
        val constructor =
            modelClass.primaryConstructor ?: throw IllegalStateException("주생성자를 가져올 수 없습니다")

        val argument = getArgument(constructor)
        val instance = constructor.call(*argument.toTypedArray()) as T
        injectField(instance)
        return instance
    }

    private fun getArgument(constructor: KFunction<Any>): List<Any> {
        val argument = constructor.parameters.map { parameter ->
            val annotation = parameter.findAnnotation<Qualifier>()
            val type = annotation?.clazz ?: parameter.type.jvmErasure
            container.getInstance(type) ?: createInstance(type)
        }
        return argument
    }

    fun <T : Any> injectField(instance: T) {
        val properties =
            instance::class.declaredMemberProperties.filter { it.hasAnnotation<InjectField>() }
        properties.forEach { property ->
            property.isAccessible = true
            val injectValue = container.getInstance(property.returnType.jvmErasure)
            (property as KMutableProperty<*>).setter.call(instance, injectValue)
        }
    }
}
