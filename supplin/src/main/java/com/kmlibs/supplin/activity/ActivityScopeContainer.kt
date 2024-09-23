package com.kmlibs.supplin.activity

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.kmlibs.supplin.annotations.ActivityContext
import com.kmlibs.supplin.application.ApplicationScopeContainer
import com.kmlibs.supplin.base.ComponentContainer
import com.kmlibs.supplin.model.QualifiedContainerType
import kotlin.reflect.KClass
import kotlin.reflect.KType

class ActivityScopeContainer private constructor(
    private val activity: ComponentActivity,
    module: KClass<*>,
) : DefaultLifecycleObserver,
    ComponentContainer(module) {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        saveInstancesFromModuleFunctions()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        containers.remove(qualifiedContainerType)
        owner.lifecycle.removeObserver(this)
        super.onDestroy(owner)
    }

    override fun resolveInstance(returnType: KType, annotations: List<Annotation>): Any {
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
            mutableMapOf<QualifiedContainerType, ActivityScopeContainer>()

        private lateinit var qualifiedContainerType: QualifiedContainerType

        fun <T : ComponentActivity> containerOf(
            activity: T,
            module: KClass<*>
        ): ActivityScopeContainer {
            qualifiedContainerType = QualifiedContainerType(module, activity::class.simpleName)
            return containers.getOrPut(qualifiedContainerType) {
                ActivityScopeContainer(activity, module).also {
                    activity.lifecycle.addObserver(it)
                }
            }
        }
    }
}

