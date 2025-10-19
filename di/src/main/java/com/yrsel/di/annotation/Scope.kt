package com.yrsel.di.annotation

@Retention(AnnotationRetention.RUNTIME)
annotation class Scope

@Scope
annotation class SingletonScope

@Scope
annotation class ApplicationScope

@Scope
annotation class ActivityScope

@Scope
annotation class ViewModelScope
