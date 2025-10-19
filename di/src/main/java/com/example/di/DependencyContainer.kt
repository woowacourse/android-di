package com.example.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties

object DependencyContainer {
    private val scopeMapping: MutableMap<Identifier, Scope> = mutableMapOf()

    private val dependencyGettersByScope: Map<Scope, MutableMap<Identifier, () -> Any>> =
        mapOf(
            Scope.APPLICATION to mutableMapOf(),
            Scope.ACTIVITY to mutableMapOf(),
            Scope.VIEWMODEL to mutableMapOf(),
        )

    fun initialize(
        application: Application,
        vararg module: Module,
    ) {
        registerActivityLifecycleCallbacks(application)
        module.forEach(::initialize)
    }

    fun dependency(identifier: Identifier): Any {
        val scope: Scope = scopeMapping[identifier] ?: error("No scope defined for $identifier")
        return dependencyGettersByScope[scope]?.get(identifier)?.invoke()
            ?: error("No dependency defined for $identifier with $scope.")
    }

    private fun initialize(module: Module) {
        module::class.memberProperties.forEach { property: KProperty1<out Module, *> ->
            if (!property.hasAnnotation<Dependency>()) return@forEach

            val identifier = Identifier.from(property)
            val scope: Scope = Scope.from(property)
            scopeMapping[identifier] = scope
            dependencyGettersByScope[scope]?.let { getters: MutableMap<Identifier, () -> Any> ->
                getters[identifier] = {
                    property.getter.call(module) ?: error("$property's getter returned null.")
                }
            } ?: error("No dependencies defined for $scope")
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

                override fun onActivityDestroyed(activity: Activity) {
                    dependencyGettersByScope[Scope.ACTIVITY]?.clear()
                }
            },
        )
    }
}
