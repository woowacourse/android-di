package com.woosuk.scott_di

interface Module {
    operator fun plus(module: Module) = listOf(module, this)
}
