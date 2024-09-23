package com.android.di.component

import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.isAccessible

class DiInjector(
    val diContainer: DiContainer,
) {
    fun addModule(module: Module) {
        module::class.declaredFunctions.forEach { kFunction ->
            kFunction.isAccessible = true
            kFunction.call(module, diContainer)
        }
    }
}
