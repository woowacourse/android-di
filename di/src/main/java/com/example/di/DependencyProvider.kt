package com.example.di

import kotlin.reflect.KClass

interface DependencyProvider {
    fun <T : Any> getInstance(kClass: KClass<*>): T?

    fun <T : Any> getInstance(
        kClass: KClass<*>,
        type: DependencyType,
    ): T?
}
