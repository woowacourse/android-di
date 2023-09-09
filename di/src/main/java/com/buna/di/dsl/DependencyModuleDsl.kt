package com.buna.di.dsl

import com.buna.di.injector.DependencyInjector
import com.buna.di.module.Module

class DependencyModuleDsl {
    fun module(module: Module) {
        DependencyInjector.module(module)
    }
}

fun modules(block: DependencyModuleDsl.() -> Unit) {
    DependencyModuleDsl().block()
}