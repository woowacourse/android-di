package com.buna.di.util

import com.buna.di.injector.DependencyInjector
import com.buna.di.injector.DependencyKey
import com.buna.di.util.diAssistant.SubTypeConverter
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

fun <T : Any> KClass<T>.validateHasPrimaryConstructor(): KFunction<T> {
    return requireNotNull(primaryConstructor) { "[ERROR] 주생성자가 존재하지 않습니다." }
}

fun <T> KFunction<T>.createInstance(converter: SubTypeConverter): T {
    return call(*parameters.createInstances(converter))
}

fun List<KParameter>.createInstances(converter: SubTypeConverter): Array<Any> = map { parameter ->
    val paramDependencyKey = DependencyKey.createDependencyKey(parameter)
    val paramType = parameter.type
    val subType = converter.convertType(paramDependencyKey, paramType)
    DependencyInjector.inject(subType.jvmErasure)
}.toTypedArray()
