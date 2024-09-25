package com.kmlibs.supplin.activity

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.kmlibs.supplin.annotations.ActivityContext
import com.kmlibs.supplin.application.ApplicationScopeContainer
import com.kmlibs.supplin.base.ComponentContainer
import kotlin.reflect.KClass
import kotlin.reflect.KType

class ActivityScopeContainer private constructor(
    private val activity: ComponentActivity,
    vararg modules: KClass<*>,
) : DefaultLifecycleObserver,
    ComponentContainer(*modules) {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        saveInstancesFromModuleFunctions()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        containers.remove(activity::class)
        owner.lifecycle.removeObserver(this)
        super.onDestroy(owner)
    }

    override fun resolveInstance(
        returnType: KType,
        annotations: List<Annotation>,
    ): Any {
        if (shouldResolveContext(returnType, annotations)) return activity
        val qualifiedType = buildQualifiedType(returnType, annotations)
        return instances[qualifiedType] ?: buildInstanceOf(qualifiedType)
        ?: ApplicationScopeContainer.container.resolveInstance(returnType, annotations)
    }

    private fun shouldResolveContext(
        returnType: KType,
        annotations: List<Annotation>,
    ): Boolean {
        val contextAnnotation = annotations.filterIsInstance<ActivityContext>().firstOrNull()
        return contextAnnotation != null && returnType.classifier == Context::class
    }

    companion object {
        private val containers =
            mutableMapOf<KClass<out ComponentActivity>, ActivityScopeContainer>()

        fun <T : ComponentActivity> containerOf(
            activity: T,
            vararg modules: KClass<*>,
        ): ActivityScopeContainer {
            return containers.getOrPut(activity::class) {
                ActivityScopeContainer(activity, *modules).also {
                    activity.lifecycle.addObserver(it)
                }
            }
        }

        fun <T : ComponentActivity> removeContainer(activity: T) {
            containers.remove(activity::class)
        }
    }
}
