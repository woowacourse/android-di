package com.re4rk.arkdiAndroid.arkGenerator

fun arkGenerator(block: ArkGeneratorBuilder.() -> Unit): ArkGenerator {
    val builder = ArkGeneratorBuilder()
    builder.block()
    return builder.build()
}
