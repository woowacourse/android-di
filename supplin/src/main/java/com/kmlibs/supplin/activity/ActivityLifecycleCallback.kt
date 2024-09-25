package com.kmlibs.supplin.activity

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.kmlibs.supplin.Injector
import com.kmlibs.supplin.annotations.Module
import com.kmlibs.supplin.annotations.SupplinActivity
import com.kmlibs.supplin.annotations.Within
import com.kmlibs.supplin.model.Scope
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

class ActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?,
    ) {
        val targetAnnotation = activity::class.findAnnotation<SupplinActivity>() ?: return
        require(activity is ComponentActivity) { EXCEPTION_INVALID_ACTIVITY_TYPE }

        val modules =
            targetAnnotation.modules.onEach { module ->
                require(module.hasAnnotation<Module>()) { EXCEPTION_NO_MODULE_ANNOTATION }
                require(module.findAnnotation<Within>()?.scope == Scope.Activity::class) {
                    EXCEPTION_NO_WITHIN_ANNOTATION
                }
            }

        Injector.setModules {
            activityModule(activity, *modules)
        }
    }

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityDestroyed(activity: Activity) {
        activity::class.findAnnotation<SupplinActivity>() ?: return
        require(activity is ComponentActivity) {
            EXCEPTION_ACTIVITY_ANNOTATION_NOT_FOUND
        }
        ActivityScopeContainer.removeContainer(activity)
        Injector.setModules { removeModuleByComponent(activity::class) }
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle,
    ) {}

    companion object {
        private const val EXCEPTION_INVALID_ACTIVITY_TYPE =
            "Activity should be ComponentActivity with @SupplinActivity annotation"
        private const val EXCEPTION_NO_MODULE_ANNOTATION =
            "Module should be annotated with @Module"
        private const val EXCEPTION_NO_WITHIN_ANNOTATION =
            "Modules in ActivityScope should be annotated with @Within(Scope.Activity::class)"
        private const val EXCEPTION_ACTIVITY_ANNOTATION_NOT_FOUND =
            "Activity should be ComponentActivity with @SupplinActivity annotation"
    }
}
