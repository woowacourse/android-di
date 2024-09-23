package com.woowacourse.di

import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.isAccessible

class DiInjector(
    val diContainer: DiContainer,
) {
    fun addModule(diModule: DiModule) {
        diModule::class.declaredFunctions.forEach { kFunction ->
            kFunction.isAccessible = true
            kFunction.call(diModule, diContainer)
        }
    }
}
