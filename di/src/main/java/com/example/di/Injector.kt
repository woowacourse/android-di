package com.example.di

import kotlin.reflect.KClass

object Injector {
    private lateinit var instanceContainer: InstanceContainer

    fun init(sourceContainer: SourceContainer) {
        instanceContainer = InstanceContainer(sourceContainer)
    }

    fun inject(classType: KClass<*>) = instanceContainer.inject(classType)
}
