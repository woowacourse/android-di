package com.buna.di.util

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

fun <T : Any> KClass<T>.validateHasPrimaryConstructor(): KFunction<T> {
    return requireNotNull(primaryConstructor) { "[ERROR] 주생성자가 존재하지 않습니다." }
}
