package com.example.di

import kotlin.reflect.KClass

object Injector {
    private lateinit var instanceContainer: InstanceContainer

    fun init(sourceContainer: SourceContainer) {
        instanceContainer = InstanceContainer(sourceContainer)
    }

    fun inject(classType: KClass<*>) = instanceContainer.inject(classType)

    fun <T : Any> injectFields(targetInstance: T) {
        instanceContainer.injectFields(targetInstance)
    }

    fun deleteInstance(classType: KClass<*>) {
        instanceContainer.deleteInstance(classType)
    }
}
