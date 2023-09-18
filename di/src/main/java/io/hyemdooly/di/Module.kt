package io.hyemdooly.di

import io.hyemdooly.di.annotation.Inject
import io.hyemdooly.di.annotation.Qualifier
import io.hyemdooly.di.annotation.Singleton
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

open class Module(private val parentModule: Module? = null) {
    private val instances = mutableMapOf<KClass<*>, Any>()

    private fun addInstance(instance: Any) {
        instances[instance::class] = instance
    }

    fun <T : Any> getInstance(type: KClass<T>): T {
        // 싱글톤에서 찾기
        if (instances[type] != null) return instances[type] as T
        val key = instances.keys.firstOrNull { it.isSubclassOf(type) }
        if (key != null && instances[key] != null) return instances[key] as T

        // 선언된 멤버 함수에서 찾기
        this::class.declaredMemberFunctions.firstOrNull { it.hasAnnotation<Singleton>() && it.returnType.jvmErasure == type }
            ?.let { provider ->
                val instance = provider.callBy(getParamInstances(provider.parameters))
                    ?: throw NoSuchElementException("declared member functions should not be null")
                instances[provider.returnType.jvmErasure] = instance
                return instance as T
            }

        // 그래도 없으면 부모 모듈에서 찾기
        val parentInstance = parentModule?.getInstance<T>(type)
        if (parentInstance != null) return parentInstance

        // 없으면 생성하기
        return createInstance(type)
    }

    private fun <T : Any> createInstance(modelClass: KClass<*>): T {
        val constructor = modelClass.primaryConstructor
        requireNotNull(constructor) { "Unknown ViewModel Class $modelClass" }

        val paramInstances = getParamInstances(constructor)
        val instance = constructor.call(*paramInstances.toTypedArray()) as T
        return instance.also { injectFields(it) }
    }

    private fun getParamInstances(parameters: List<KParameter>): Map<KParameter, Any?> {
        return parameters.associateWith { param ->
            when {
                param.type.jvmErasure.isSubclassOf(Module::class) -> this@Module
                else -> getInstance(param.type.jvmErasure)
            }
        }
    }

    private fun <T : Any> getParamInstances(constructor: KFunction<T>): List<Any?> {
        val paramInstances = constructor.parameters.map { param ->
            val annotation = param.findAnnotation<Qualifier>()
            val type = annotation?.clazz ?: param.type.jvmErasure
            getInstance(type)
        }
        return paramInstances
    }

    fun <T : Any> injectFields(instance: T) {
        val properties =
            instance::class.declaredMemberProperties.filter { it.hasAnnotation<Inject>() }

        properties.forEach { property ->
            property.isAccessible = true
            property.javaField?.let {
                val type = it.type.kotlin
                val fieldValue = getInstance(type)
                it.set(instance, fieldValue)
            }
        }
    }
}
