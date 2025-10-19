package com.example.di

import androidx.lifecycle.SavedStateHandle
import com.example.di.scope.AppScope
import com.example.di.scope.AppScopeHandler
import com.example.di.scope.ScopeContainer
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object DependencyInjector {
    private val instances = mutableMapOf<DependencyKey<*>, () -> Any>()

    fun setInstance(container: Any) {
        container::class
            .members
            .filterIsInstance<KProperty1<Any, *>>()
            .forEach { property ->
                property.isAccessible = true
                val kClass = property.returnType.classifier as KClass<*>
                val qualifier = findAnnotation(property)
                val key = DependencyKey(kClass, qualifier)
                instances[key] = { property.get(container)!! }
            }
    }

    fun <T : Any> getInstance(
        kClass: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        val key = DependencyKey(kClass, qualifier)

        val instance = instances[key]?.invoke()
        return instance as? T ?: error("등록되지 않은 의존성: $kClass, qualifier=$qualifier")
    }

    fun <T : Any> injectAnnotatedProperties(
        kClass: KClass<T>,
        instance: T,
    ) {
        kClass.members
            .filterIsInstance<KMutableProperty1<Any, Any>>()
            .forEach { property ->
                val qualifier = findAnnotation(property)
                val requireInjection = property.findAnnotation<RequireInjection>() ?: return@forEach
                property.isAccessible = true

                val implClass = requireInjection.impl
                val handler =
                    ScopeContainer.getHandler(requireInjection.scope)
                        ?: error("등록 안된 스코프 ${requireInjection.scope}")

                val dependencyInstance =
                    handler.getInstance(
                        kClass = implClass,
                        qualifier = qualifier,
                        savedStateHandle = null,
                        context = null,
                    )
                property.setter.call(instance, dependencyInstance)

                if (requireInjection.scope == AppScope::class) {
                    AppScopeHandler.putInstance(
                        dependencyInstance::class,
                        qualifier,
                        dependencyInstance,
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

    fun <T : Any> createInstance(
        kClass: KClass<T>,
        handle: SavedStateHandle?,
        key: DependencyKey<*>,
    ): T {
        val primaryConstructor = kClass.primaryConstructor
        primaryConstructor?.let {
            val instance = createPrimaryConstructor(it, handle, key.qualifier)
            injectAnnotatedProperties(kClass, instance as T)
            return instance
        }

        kClass.objectInstance?.let { obj ->
            injectAnnotatedProperties(kClass, obj)
            return obj
        }

        val defaultConstructor =
            kClass.java.getDeclaredConstructor().newInstance()
        injectAnnotatedProperties(kClass, defaultConstructor)
        return defaultConstructor
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
