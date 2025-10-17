package com.example.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties

object DependencyContainer {
    private val dependencyGetters: MutableMap<Identifier, () -> Any> = mutableMapOf()

    private val applicationDependencyGetters: MutableMap<Identifier, () -> Any> = mutableMapOf()
    private val viewModelDependencyGetters: MutableMap<Identifier, () -> Any> = mutableMapOf()
    private val activityDependencyGetters: MutableMap<Identifier, () -> Any> = mutableMapOf()

    fun initialize(
        application: Application,
        vararg module: Module,
    ) {
        registerActivityLifecycleCallbacks(application)
        module.forEach(::initialize)
    }

    fun dependency(identifier: Identifier): Any = dependencyGetters[identifier]?.invoke() ?: error("No dependency defined for $identifier.")

    private fun initialize(module: Module) {
        module::class.memberProperties.forEach { property: KProperty1<out Module, *> ->
            if (!property.hasAnnotation<Dependency>()) return@forEach

            val identifier = Identifier.from(property)
            dependencyGetters[identifier] = {
                property.getter.call(module) ?: error("$property's getter returned null.")
            }
        }
    }

    private fun registerActivityLifecycleCallbacks(application: Application) {
        application.registerActivityLifecycleCallbacks(
            object :
                Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(
                    activity: Activity,
                    bundle: Bundle?,
                ) = Unit

                override fun onActivityStarted(activity: Activity) = Unit

                override fun onActivityPaused(activity: Activity) = Unit

                override fun onActivitySaveInstanceState(
                    activity: Activity,
                    bundle: Bundle,
                ) = Unit

                override fun onActivityResumed(activity: Activity) = Unit

                override fun onActivityStopped(activity: Activity) = Unit

                override fun onActivityDestroyed(activity: Activity) = activityDependencyGetters.clear()
            },
        )
    }
}
