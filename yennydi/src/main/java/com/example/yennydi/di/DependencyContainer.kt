package com.example.yennydi.di

import kotlin.reflect.KClass

interface DependencyContainer {
    fun <T : Any> getInstance(
        kClass: KClass<*>,
        annotation: Annotation? = null,
    ): T?

    fun getImplementationClass(
        kClass: KClass<*>,
        annotation: Annotation? = null,
    ): KClass<out Any>

    fun <T : Any> addInstance(
        kClass: KClass<*>,
        instance: T,
    )

    fun addDeferredDependency(vararg dependencies: Pair<KClass<out Any>, KClass<out Any>>)

    fun clear()
}
