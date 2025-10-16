package com.example.di.wrapper

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import com.example.di.AppContainer

class ActivityFieldInjector(
    private val container: AppContainer,
) : Application.ActivityLifecycleCallbacks {
    override fun onActivityPreCreated(
        activity: Activity,
        savedInstanceState: Bundle?,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) container.injectProperties(activity)
    }

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?,
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) container.injectProperties(activity)
    }

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityResumed(activity: Activity) = Unit

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle,
    ) = Unit

    override fun onActivityDestroyed(activity: Activity) = Unit
}
