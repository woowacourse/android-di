package com.daedan.di

interface DiComponent {
    val appContainerStore: AppContainerStore

    fun register(vararg modules: DependencyModule) {
        appContainerStore.registerFactory(*modules)
    }
}
