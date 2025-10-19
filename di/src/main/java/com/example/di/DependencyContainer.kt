package com.example.di

import android.app.Activity
import android.app.Application
import android.os.Bundle

object DependencyContainer {
    private lateinit var dependencyMapping: DependencyMapping

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
                    dependencyMapping.clear(Scope.ACTIVITY)
                }
            },
        )
    }
}
