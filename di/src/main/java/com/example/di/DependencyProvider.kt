package com.example.di

import kotlin.reflect.KClass

interface DependencyProvider {
    fun addInstanceDependency(vararg dependencies: Pair<KClass<out Any>, Any>)

    fun addDeferredDependency(vararg dependencies: Pair<KClass<out Any>, KClass<out Any>>)

    fun getImplementationClass(
        kClass: KClass<*>,
        annotation: Annotation? = null,
    ): KClass<out Any>

    fun <T : Any> getInstance(
        kClass: KClass<*>,
        annotation: Annotation? = null,
    ): T?

    fun clear()
}
