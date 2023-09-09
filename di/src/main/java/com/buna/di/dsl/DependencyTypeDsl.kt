package com.buna.di.dsl

import com.buna.di.injector.DependencyInjector
import kotlin.reflect.KClass

class DependencyTypeDsl {
    fun <T : Any> type(pairClass: Pair<KClass<T>, KClass<*>>) {
        val superClass = pairClass.first
        val subClass = pairClass.second
        DependencyInjector.type(superClass, subClass)
    }
}

fun types(block: DependencyTypeDsl.() -> Unit) {
    DependencyTypeDsl().block()
}
