package com.woowacourse.bunadi.dsl

import com.woowacourse.bunadi.injector.SingletonDependencyInjector
import com.woowacourse.bunadi.module.Module

class DependencyModuleDsl {
    fun module(module: Module) {
        SingletonDependencyInjector.module(module)
    }
}

fun modules(block: DependencyModuleDsl.() -> Unit) {
    DependencyModuleDsl().block()
}
