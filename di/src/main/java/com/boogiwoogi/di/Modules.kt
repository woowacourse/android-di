package com.boogiwoogi.di

import kotlin.reflect.KClass

/**
 * the class that provide how to instantiate some classes
 */
interface Modules {

    fun provideInstanceOf(clazz: KClass<*>): Any?

    fun provideInstanceOf(simpleName: String): Any?
}
