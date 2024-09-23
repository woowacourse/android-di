package com.zzang.di

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import com.zzang.di.annotation.QualifierType
import com.zzang.di.annotation.lifecycle.ActivityComponent
import com.zzang.di.annotation.lifecycle.ApplicationComponent
import com.zzang.di.annotation.lifecycle.ViewModelComponent
import com.zzang.di.module.DIModule
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

object DIContainer {
    private val applicationScopedInstances = mutableMapOf<String, Any>()
    private val activityScopedInstances = mutableMapOf<ComponentActivity, MutableMap<String, Any>>()
    private val viewModelScopedInstances = mutableMapOf<ViewModel, MutableMap<String, Any>>()

    fun loadModule(module: DIModule) {
        module.register(this)
    }

    private fun buildKey(
        type: KClass<*>,
        qualifier: QualifierType? = null,
    ): String {
        return "${type.qualifiedName}_${qualifier?.name}"
    }

    fun <T : Any> registerComponentScopedInstance(
        type: KClass<T>,
        instance: T,
        qualifier: QualifierType? = null,
        owner: Any? = null,
    ) {
        Log.d("DIContainer 어노테이션", "${type.simpleName}=${type.annotations}$owner")
        when {
            type.findAnnotation<ApplicationComponent>() != null -> {
                Log.d("DIContainer", "registerSingletonInstance: ${type.simpleName}")
                registerSingletonInstance(type, instance, qualifier)
            }

            type.findAnnotation<ActivityComponent>() != null && owner is ComponentActivity -> {
                Log.d("DIContainer", "registerActivityScopedInstance: ${type.simpleName}")
                registerActivityScopedInstance(owner, type, instance)
            }

            type.findAnnotation<ViewModelComponent>() != null && owner is ViewModel -> {
                Log.d("DIContainer", "registerViewModelScopedInstance: ${type.simpleName}")
                registerViewModelScopedInstance(owner, type, instance)
            }

            else -> {
                registerSingletonInstance(type, instance, qualifier)
            }
        }
    }

    fun <T : Any> registerSingletonInstance(
        interfaceClass: KClass<T>,
        instance: T,
        qualifier: QualifierType? = null,
    ) {
        val key = buildKey(interfaceClass, qualifier)
        applicationScopedInstances[key] = instance
    }

    private fun <T : Any> registerActivityScopedInstance(
        activity: ComponentActivity,
        type: KClass<T>,
        instance: T,
    ) {
        val key = buildKey(type)
        activityScopedInstances.getOrPut(activity) { mutableMapOf() }[key] = instance
    }

    private fun <T : Any> registerViewModelScopedInstance(
        viewModel: ViewModel,
        type: KClass<T>,
        instance: T,
    ) {
        val key = buildKey(type)
        viewModelScopedInstances.getOrPut(viewModel) { mutableMapOf() }[key] = instance
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(
        type: KClass<T>,
        qualifier: QualifierType? = null,
        owner: Any? = null,
    ): T {
        val key = buildKey(type, qualifier)
        applicationScopedInstances[key]?.let { return it as T }

        if (type == Context::class) {
            return owner as T
        }

        if (owner is ComponentActivity) {
            registerActivityScopedInstance(owner, type, createInstance(type, qualifier, owner))
        }

        if (owner is ViewModel) {
            registerViewModelScopedInstance(owner, type, createInstance(type, qualifier, owner))
        }

        return createInstance(type, qualifier, owner)
    }

    private fun <T : Any> createInstance(
        type: KClass<T>,
        qualifier: QualifierType? = null,
        owner: Any? = null,
    ): T {
        val constructor = type.primaryConstructor
        if (constructor != null) {
            val parameters =
                constructor.parameters.map { parameter ->
                    val parameterType = parameter.type.classifier as KClass<*>
                    resolve(parameterType, qualifier = qualifier, owner = owner)
                }
            return constructor.call(*parameters.toTypedArray())
        }

        throw IllegalArgumentException("${type.simpleName} 클래스에 사용할 수 있는 생성자가 없습니다. 이미 등록된 인스턴스를 사용하십시오.")
    }

    fun clearActivityScopedInstances(activity: ComponentActivity) {
        Log.d("DIContainer", "clearActivityScopedInstances: ${activityScopedInstances.values}")
        activityScopedInstances.remove(activity)
    }

    fun clearViewModelScopedInstances(viewModel: ViewModel) {
        Log.d("DIContainer", "clearViewModelScopedInstances: ${viewModelScopedInstances.values}")
        viewModelScopedInstances.remove(viewModel)
    }
}
