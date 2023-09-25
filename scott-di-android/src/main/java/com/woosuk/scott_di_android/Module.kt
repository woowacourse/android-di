package com.woosuk.scott_di_android

interface Module {
    operator fun plus(module: Module) = listOf(module, this)
}
