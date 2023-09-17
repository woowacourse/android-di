package com.boogiwoogi.di.version2

import kotlin.reflect.KClass

interface Modules {

    fun provideInstanceOf(clazz: KClass<*>): Any?

    fun provideInstanceOf(simpleName: String): Any?
}