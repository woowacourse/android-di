package com.example.di

import kotlin.reflect.KClass

interface DependencyContainer {
    fun <T : Any> getInstance(
        kClass: KClass<*>,
        annotation: Annotation? = null,
    ): T?

    fun <T : Any> add(instance: T)

    fun clear()
}
