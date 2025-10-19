package com.example.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.ViewModel
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.typeOf

object DependencyContainer {
    private lateinit var dependencyMapping: DependencyMapping

    private val liveViewModelOwners: MutableSet<KClass<out Activity>> = mutableSetOf()
    private val liveActivities: MutableSet<KClass<out Activity>> = mutableSetOf()

    fun initialize(
        application: Application,
        vararg module: Module,
    ) {
        registerActivityLifecycleCallbacks(application)
        dependencyMapping = DependencyMapping(*module)
    }

    fun dependency(identifier: Identifier): Any = dependencyMapping.get(identifier)

    private fun registerActivityLifecycleCallbacks(application: Application) {
        application.registerActivityLifecycleCallbacks(
            object :
                Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(
                    activity: Activity,
                    bundle: Bundle?,
                ) {
                    DependencyInjector.injectFields(activity)
                    liveActivities.add(activity::class)
                    if (activity.hasViewModel()) liveViewModelOwners.add(activity::class)
                }

                override fun onActivityStarted(activity: Activity) = Unit

                override fun onActivityPaused(activity: Activity) = Unit

                override fun onActivitySaveInstanceState(
                    activity: Activity,
                    bundle: Bundle,
                ) = Unit

                override fun onActivityResumed(activity: Activity) = Unit

                override fun onActivityStopped(activity: Activity) = Unit

                override fun onActivityDestroyed(activity: Activity) {
                    if (activity.isChangingConfigurations) return

                    liveActivities.remove(activity::class)
                    if (liveActivities.isEmpty()) dependencyMapping.clear(Scope.ACTIVITY)

                    if (activity.isFinishing) {
                        liveViewModelOwners.remove(activity::class)
                        if (liveViewModelOwners.isEmpty()) dependencyMapping.clear(Scope.VIEWMODEL)
                    }
                }
            },
        )
    }

    private fun Activity.hasViewModel(): Boolean =
        this::class.memberProperties.any { property: KProperty1<out Activity, *> ->
            property.returnType.isSubtypeOf(typeOf<ViewModel>())
        }
}
