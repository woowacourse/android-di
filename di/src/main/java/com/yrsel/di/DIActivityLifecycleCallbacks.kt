package com.yrsel.di

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

object DIActivityLifecycleCallbacks : ActivityLifecycleCallbacks {
    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?,
    ) {
        ContextProvider.register(activity)
        DependencyInjector.injectFields(instance = activity, identifier = activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        ContextProvider.unRegister(activity)
        ScopeContainer.clear(ScopeType.Activity(activity))
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle,
    ) {
    }

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}
}
