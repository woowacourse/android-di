package com.dygames.di.lifecycle

interface LifecycleWatcher {

    fun createDependencies()
    fun destroyDependencies()
}
