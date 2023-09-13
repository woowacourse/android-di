package com.woowacourse.bunadi.dsl

import com.woowacourse.bunadi.injector.DependencyInjector
import com.woowacourse.bunadi.module.Module

class DependencyModuleDsl {
    fun module(module: Module) {
        DependencyInjector.module(module)
    }
}

fun modules(block: DependencyModuleDsl.() -> Unit) {
    DependencyModuleDsl().block()
}
