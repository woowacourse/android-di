package com.boogiwoogi.di.version2

import kotlin.reflect.KClass
import kotlin.reflect.KParameter

interface DiContainer {

    var value: MutableList<Instance<out Any>>?

    fun add(instance: Instance<*>)

    fun find(parameter: KParameter): Any?

    fun find(clazz: KClass<*>): Any?

    fun find(simpleName: String?): Any?
}
