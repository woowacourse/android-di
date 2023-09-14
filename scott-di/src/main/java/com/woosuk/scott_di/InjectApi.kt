package com.woosuk.scott_di

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

// DI 시작
fun startDI(block: DiContainer.() -> Unit) = DiContainer.apply(block)

inline fun <reified T : Any> inject(): T {
    return DiContainer.inject(T::class)
}
