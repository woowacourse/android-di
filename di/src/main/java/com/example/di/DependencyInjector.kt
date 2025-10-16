package com.example.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object DependencyInjector {
    private val instances = mutableMapOf<DependencyKey<*>, () -> Any>()
    private val creating = mutableSetOf<KClass<*>>()

    fun setInstance(container: Any) {
        container::class
            .members
            .filterIsInstance<KProperty1<Any, *>>()
            .forEach { property ->
                property.isAccessible = true
                val instance = property.get(container) ?: return@forEach
                val kClass = property.returnType.classifier as KClass<*>
                val qualifier = findAnnotation(property)
                val key = DependencyKey(kClass, qualifier)
                instances[key] = { property.get(container)!! }
            }
    }

    fun <T : Any> getInstance(
        kClass: KClass<T>,
        savedStateHandle: SavedStateHandle? = null,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        val key = DependencyKey(kClass, qualifier)
        val isViewModel = kClass.findAnnotation<ViewModelScope>() != null
        if (isViewModel && !ViewModel::class.java.isAssignableFrom(kClass.java)) error("${kClass.java.simpleName}:viewModel만 사용 가능한 어노테이션")
        if (ViewModel::class.java.isAssignableFrom(kClass.java) && !isViewModel) error("${kClass.java.simpleName}:@ViewModelScope 필요")

        return if (isViewModel) {
            createInstance(kClass, savedStateHandle, key) as T
        } else {
            val factory =
                instances[key] ?: run {
                    val newFactory = { createInstance(kClass, savedStateHandle, key) }
                    instances[key] = newFactory
                    newFactory
                }

            val instance = factory.invoke() as T

            instances[key] = { instance }

            return instance
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

                val qualifier = findAnnotation(property)

                dependencyClass?.let {
                    property.setter.call(
                        instance,
                        getInstance(it, qualifier = qualifier),
                    )
                }
            }
    }

    private fun findAnnotation(property: KProperty1<Any, *>) =
        when {
            property.findAnnotation<InMemoryLogger>() != null -> InMemoryLogger::class
            property.findAnnotation<DatabaseLogger>() != null -> DatabaseLogger::class
            else -> null
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
