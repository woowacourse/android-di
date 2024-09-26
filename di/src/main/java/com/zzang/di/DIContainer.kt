package com.zzang.di

import android.content.Context
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
    private val moduleInstances = mutableMapOf<Dependency, Any>()
    private val applicationScopedInstances = mutableMapOf<Dependency, Any>()
    private val activityScopedInstances = mutableMapOf<ComponentActivity, MutableMap<Dependency, Any>>()
    private val viewModelScopedInstances = mutableMapOf<ViewModel, MutableMap<Dependency, Any>>()

    fun loadModule(module: DIModule) {
        module.register(this)
    }

    private fun buildDependency(
        type: KClass<*>,
        qualifier: QualifierType? = null,
    ): Dependency {
        return Dependency(type, qualifier)
    }

    fun <T : Any> registerModuleInstance(
        type: KClass<T>,
        instance: T,
        qualifier: QualifierType? = null,
    ) {
        val key = buildDependency(type, qualifier)
        moduleInstances[key] = instance
    }

    private fun <T : Any> registerComponentScopedInstance(
        type: KClass<T>,
        instance: T,
        qualifier: QualifierType? = null,
        owner: Any? = null,
    ) {
        when {
            type.findAnnotation<ApplicationComponent>() != null -> {
                registerSingletonInstance(type, instance, qualifier)
            }

            type.findAnnotation<ActivityComponent>() != null && owner is ComponentActivity -> {
                registerActivityScopedInstance(owner, type, instance)
            }

            type.findAnnotation<ViewModelComponent>() != null && owner is ViewModel -> {
                registerViewModelScopedInstance(owner, type, instance)
            }

            else -> {
                registerSingletonInstance(type, instance, qualifier)
            }
        }
    }

    private fun <T : Any> registerSingletonInstance(
        interfaceClass: KClass<T>,
        instance: T,
        qualifier: QualifierType? = null,
    ) {
        val key = buildDependency(interfaceClass, qualifier)
        applicationScopedInstances[key] = instance
    }

    private fun <T : Any> registerActivityScopedInstance(
        activity: ComponentActivity,
        type: KClass<T>,
        instance: T,
    ) {
        val key = buildDependency(type)
        activityScopedInstances.getOrPut(activity) { mutableMapOf() }[key] = instance
    }

    private fun <T : Any> registerViewModelScopedInstance(
        viewModel: ViewModel,
        type: KClass<T>,
        instance: T,
    ) {
        val key = buildDependency(type)
        viewModelScopedInstances.getOrPut(viewModel) { mutableMapOf() }[key] = instance
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(
        type: KClass<T>,
        qualifier: QualifierType? = null,
        owner: Any? = null,
    ): T {
        val key = buildDependency(type, qualifier)

        if (type == Context::class) {
            return owner as T
        }

        moduleInstances[key]?.let { instance ->
            registerComponentScopedInstance(type, instance as T, qualifier, owner)
            return instance
        }

        return createInstance(type, qualifier, owner).also {
            registerComponentScopedInstance(type, it, qualifier, owner)
        }
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
        activityScopedInstances.remove(activity)
    }

    fun clearViewModelScopedInstances(viewModel: ViewModel) {
        viewModelScopedInstances.remove(viewModel)
    }
}
