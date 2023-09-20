package com.re4rk.arkdiAndroid.arkModules

fun arkModules(block: ArkModulesBuilder.() -> Unit): ArkModules {
    val builder = ArkModulesBuilder()
    builder.block()
    return builder.build()
}
