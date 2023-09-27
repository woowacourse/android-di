package com.angrypig.autodi.lifeCycleScopeHandler.annotation.activity

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope(val lifeCycleScope: ActivityLifeCycleScope)
