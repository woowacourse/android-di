package com.woowacourse.bunadi.dsl

import com.woowacourse.bunadi.injector.SingletonDependencyInjector
import com.woowacourse.bunadi.module.Module

class SingletonDependencyModuleDsl(
    private val singletonInjector: SingletonDependencyInjector,
) {
    fun module(module: Module) {
        singletonInjector.module(module)
    }
}

fun SingletonDependencyInjector.modules(block: SingletonDependencyModuleDsl.() -> Unit) {
    SingletonDependencyModuleDsl(this).block()
}
