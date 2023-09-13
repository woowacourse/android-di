package com.bandal.di

import kotlin.reflect.KClass

interface AppContainer {

    fun getInstance(type: KClass<*>, annotations: List<Annotation> = emptyList()): Any?

    fun addInstance(type: KClass<*>, instance: Any)

    fun clear()
}
