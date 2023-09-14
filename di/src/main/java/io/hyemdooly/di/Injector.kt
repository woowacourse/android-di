package io.hyemdooly.di

import io.hyemdooly.di.annotation.Inject
import io.hyemdooly.di.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

object Injector {
    fun <T : Any> inject(modelClass: KClass<*>): T {
        val instance = Container.getInstance(modelClass) ?: createInstance(modelClass)
        return instance as T
    }

    private fun <T : Any> createInstance(modelClass: KClass<*>): T {
        val constructor = modelClass.primaryConstructor
        requireNotNull(constructor) { "Unknown ViewModel Class $modelClass" }

        val paramInstances = getParamInstances(constructor)
        val instance = constructor.call(*paramInstances.toTypedArray()) as T
        return instance.also { injectFields(it) }
    }

    private fun <T : Any> getParamInstances(constructor: KFunction<T>): List<Any> {
        val paramInstances = constructor.parameters.map { param ->
            val annotation = param.findAnnotation<Qualifier>()
            val type = annotation?.clazz ?: param.type.jvmErasure
            Container.getInstance(type) ?: inject(type)
        }
        return paramInstances
    }

    private fun <T : Any> injectFields(instance: T) {
        val properties =
            instance::class.declaredMemberProperties.filter { it.hasAnnotation<Inject>() }

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
