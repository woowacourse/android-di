package woowacourse.shopping.di

import woowacourse.shopping.di.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

object Injector {
    fun <T> inject(modelClass: KClass<*>): T {
        val instance = Container.getInstance(modelClass) ?: createInstance(modelClass)
        return instance as T
    }

    private fun <T> createInstance(modelClass: KClass<*>): T {
        val constructor = modelClass.primaryConstructor
        requireNotNull(constructor) { "Unknown ViewModel Class $modelClass" }

        val paramInstances = getParamInstances(constructor)
        val instance = constructor.call(*paramInstances.toTypedArray()) as T
        injectFields(instance)

        return instance
    }

    private fun <T> getParamInstances(constructor: KFunction<T>): List<Any> {
        val paramInstances = constructor.parameters.map { param ->
            val type = param.type.jvmErasure
            Container.getInstance(type, param.annotations) ?: inject(type)
        }
        return paramInstances
    }

    private fun <T> injectFields(instance: T) {
        val notNullInstance = requireNotNull(instance) { "Instance should not null" }
        val properties =
            notNullInstance::class.declaredMemberProperties.filter { it.hasAnnotation<Inject>() }

        properties.forEach { property ->
            property.isAccessible = true
            property.javaField?.let {
                val type = it.type.kotlin
                val fieldValue = Container.getInstance(type) ?: inject(type)
                it.set(instance, fieldValue)
            }
        }
    }
}
