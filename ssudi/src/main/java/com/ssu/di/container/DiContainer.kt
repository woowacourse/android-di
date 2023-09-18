package com.ssu.di.container

import kotlin.reflect.KClass

interface DiContainer {
    fun <T : Any> createInstance(clazz: KClass<*>, instance: T)
    fun <T : Any> createInstance(qualifier: String, instance: T)
    fun <T : Any> getInstance(clazz: KClass<T>): T?
    fun <T : Any> getInstance(qualifier: String): T?
}
