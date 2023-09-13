package com.woowacourse.bunadi.dsl

import com.woowacourse.bunadi.injector.DependencyInjector
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
