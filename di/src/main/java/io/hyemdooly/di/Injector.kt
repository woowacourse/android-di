package io.hyemdooly.di

import io.hyemdooly.di.annotation.Inject
import io.hyemdooly.di.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

class Injector(private val parentInjector: Injector? = null) {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun addInstance(instance: Any) {
        instances[instance::class] = instance
    }

    fun addInstances(module: Module) {
        module::class.declaredMemberFunctions.forEach { provider ->
            provider.call(module)?.let {
                addInstance(it)
            }
        }
    }

    fun getInstance(type: KClass<*>): Any? {
        if (instances[type] != null) return instances[type]
        val key = instances.keys.firstOrNull { it.isSubclassOf(type) }
        if (key != null && instances[key] != null) return instances[key]

        val parentInstance = parentInjector?.getInstance(type)
        if (parentInstance != null) return parentInstance

        return createInstance(type)
    }

    fun clear() {
        instances.clear()
    }

    private fun <T : Any> inject(modelClass: KClass<*>): T {
        val instance = getInstance(modelClass) ?: createInstance(modelClass)
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
            getInstance(type) ?: inject(type)
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
                val fieldValue = getInstance(type) ?: inject(type)
                it.set(instance, fieldValue)
            }
        }
    }
}
