package com.example.di

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.lifecycle.SavedStateHandle
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
                val qualifier = findQualifier(property)
                val key = DependencyKey(kClass, qualifier)
                instances[key] = { property.get(container)!! }
            }
    }

    fun <T : Any> getOrCreateInstance(
        kClass: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        savedStateHandle: SavedStateHandle? = null,
        context: Any? = null,
        scope: KClass<out Annotation>? = null,
    ): T {
        val key = DependencyKey(kClass, qualifier)

        instances[key]?.invoke()?.let { return it as T }

        val handler = scope?.let { ScopeContainer.getHandler(it) }
        return handler?.getOrCreate(
            key = key,
            activity = context as? ComponentActivity,
        ) { createInstance(kClass, savedStateHandle, context, qualifier) }
            ?: createInstance(kClass, savedStateHandle, context, qualifier)
    }

    private fun <T : Any> createInstance(
        kClass: KClass<T>,
        handle: SavedStateHandle?,
        context: Any?,
        qualifier: KClass<out Annotation>?,
    ): T {
        val primaryConstructor = kClass.primaryConstructor
        val instance =
            primaryConstructor?.let {
                createPrimaryConstructor(it, handle, qualifier)
            } ?: kClass.java.getDeclaredConstructor().newInstance()

        injectField(instance, context)
        return instance
    }

    private fun <T : Any> createPrimaryConstructor(
        it: KFunction<T>,
        handle: SavedStateHandle?,
        qualifier: KClass<out Annotation>?,
    ): T {
        val args =
            it.parameters.associateWith { param ->
                when (val paramType = param.type.classifier) {
                    SavedStateHandle::class -> handle
                    else ->
                        instances[
                            DependencyKey(
                                paramType as KClass<*>,
                                qualifier,
                            ),
                        ]?.invoke()
                } ?: getOrCreateInstance(param.type.classifier as KClass<*>, qualifier)
            }
        return it.callBy(args)
    }

    fun <T : Any> injectField(
        instance: T,
        context: Any? = null,
    ) {
        instance::class
            .members
            .filterIsInstance<KMutableProperty1<Any, Any>>()
            .forEach { prop ->
                val requireInjection = prop.findAnnotation<RequireInjection>() ?: return@forEach
                val dependency =
                    getOrCreateInstance(
                        kClass = requireInjection.impl,
                        qualifier = findQualifier(prop),
                        context =
                            when (val activityContext = prop.findAnnotation<RequireContext>()) {
                                null -> instance
                                else ->
                                    when (activityContext.contextType) {
                                        RequireContext.ContextType.ACTIVITY -> context
                                        RequireContext.ContextType.APPLICATION -> context as? Application
                                    }
                            },
                        scope = requireInjection.scope,
                        savedStateHandle = null,
                    )

                prop.isAccessible = true
                prop.setter.call(instance, dependency)
            }
    }

    private fun findQualifier(prop: KProperty1<Any, *>): KClass<out Annotation>? =
        when {
            prop.findAnnotation<InMemoryLogger>() != null -> InMemoryLogger::class
            prop.findAnnotation<DatabaseLogger>() != null -> DatabaseLogger::class
            else -> null
        }
}
