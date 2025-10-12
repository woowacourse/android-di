package com.example.di

import androidx.lifecycle.SavedStateHandle
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object DependencyInjector {
    private val instances = mutableMapOf<DependencyKey<*>, Any>()
    private val creating = mutableSetOf<KClass<*>>()

    fun <T : Any> setInstance(
        kClass: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) {
        val key = DependencyKey(kClass, qualifier)
        instances[key] = instance
    }

    fun <T : Any> getInstance(
        kClass: KClass<T>,
        savedStateHandle: SavedStateHandle? = null,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        val key = DependencyKey(kClass, qualifier)
        return instances[key] as? T ?: run {
            instances[key] = createInstance(kClass, savedStateHandle, key)
            return (instances[key] as T).also { instances.remove(key) }
        }
    }

    fun <T : Any> injectAnnotatedProperties(
        kClass: KClass<T>,
        instance: T,
    ) {
        kClass.members
            .filterIsInstance<KMutableProperty1<Any, Any>>()
            .forEach { property ->
                if (property.findAnnotation<RequireInjection>() == null) return@forEach
                property.isAccessible = true
                val dependencyClass = property.returnType.classifier as? KClass<*>

                val qualifier =
                    when {
                        property.findAnnotation<InMemoryLogger>() != null -> InMemoryLogger::class
                        property.findAnnotation<DatabaseLogger>() != null -> DatabaseLogger::class
                        else -> null
                    }
                dependencyClass?.let {
                    property.setter.call(
                        instance,
                        getInstance(it, qualifier = qualifier),
                    )
                }
            }
    }

    private fun createInstance(
        kClass: KClass<*>,
        handle: SavedStateHandle?,
        key: DependencyKey<*>,
    ): Any {
        if (creating.contains(kClass)) throw IllegalStateException("순환 참조")
        creating.add(kClass)

        try {
            val primaryConstructor = kClass.primaryConstructor
            primaryConstructor?.let { return createPrimaryConstructor(it, handle, key.qualifier) }

            kClass.objectInstance?.let { obj -> return obj }

            return kClass.java.getDeclaredConstructor().newInstance()
        } finally {
            creating.remove(kClass)
            instances.remove(key)
        }
    }

    private fun createPrimaryConstructor(
        primaryConstructor: KFunction<Any>,
        handle: SavedStateHandle?,
        qualifier: KClass<out Annotation>?,
    ): Any {
        val params =
            primaryConstructor.parameters.associateWith { param ->
                when (param.type.classifier) {
                    SavedStateHandle::class -> requireNotNull(handle) { "SavedStateHandle 필요" }
                    else -> getInstance(param.type.classifier as KClass<*>, qualifier = qualifier)
                }
            }
        return primaryConstructor.callBy(params)
    }
}
