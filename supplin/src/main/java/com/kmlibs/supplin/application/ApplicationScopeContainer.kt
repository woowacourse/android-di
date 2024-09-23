package com.kmlibs.supplin.application

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.kmlibs.supplin.base.ComponentContainer
import com.kmlibs.supplin.annotations.ApplicationContext
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure

class ApplicationScopeContainer private constructor(
    private val applicationContext: Context,
    module: KClass<*>
) : DefaultLifecycleObserver,
    ComponentContainer(module) {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        saveInstancesFromModuleFunctions()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        _container = null
        owner.lifecycle.removeObserver(this)
        super.onDestroy(owner)
    }

    override fun resolveInstance(returnType: KType, annotations: List<Annotation>): Any {
        if (shouldResolveContext(returnType, annotations)) return applicationContext
        val qualifiedType = buildQualifiedType(returnType, annotations)
        val instance = instances[qualifiedType] ?: buildInstanceOf(qualifiedType)
        return checkNotNull(instance) { EXCEPTION_NULL_INSTANCE.format(returnType.jvmErasure) }
    }

    private fun shouldResolveContext(
        returnType: KType,
        annotations: List<Annotation>,
    ): Boolean {
        val contextAnnotation = annotations.filterIsInstance<ApplicationContext>().firstOrNull()
        return contextAnnotation != null && returnType.classifier == Context::class
    }

    companion object {
        private const val EXCEPTION_NULL_INSTANCE = "Failed to create instance for the function: %s"
        private var _container: ApplicationScopeContainer? = null
        val container: ApplicationScopeContainer
            get() = _container!!

        fun <T : Any> containerOf(
            applicationContext: Context,
            module: KClass<T>,
        ): ApplicationScopeContainer {
            _container =
                ApplicationScopeContainer(applicationContext, module).also {
                    ProcessLifecycleOwner.get().lifecycle.addObserver(it)
                }
            return container
        }
    }
}

