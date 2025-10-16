package com.daedan.di

import android.app.Application

abstract class DiApplication : Application() {
    val appContainerStore = AppContainerStore()

    fun register(vararg modules: DependencyModule) {
        appContainerStore.registerFactory(*modules)
    }
}
