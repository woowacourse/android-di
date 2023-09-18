package com.dygames.android_di

import com.dygames.di.DependencyInjector
import kotlin.reflect.typeOf

object AndroidDependencyInjector {
    inline fun <reified T : Any> inject(): T {
        return DependencyInjector.inject(typeOf<T>()) as T
    }
}
