package com.boogiwoogi.di

import kotlin.reflect.KClass

interface WoogiContainer {

    fun <T : Any> declareDependency(dependency: Dependency<T>)

    fun find(clazz: KClass<*>): Any?
}
