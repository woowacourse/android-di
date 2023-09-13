package com.woowacourse.bunadi.util

import com.woowacourse.bunadi.annotation.Qualifier
import com.woowacourse.bunadi.injector.DependencyInjector
import com.woowacourse.bunadi.injector.DependencyKey
import com.woowacourse.bunadi.util.core.SubTypeConverter
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
    val annotation = parameter.annotations.firstOrNull()
    val qualifier = annotation?.annotationClass
    val qualifier2 = qualifier?.annotations?.find { it is Qualifier } as? Qualifier
    val realType = qualifier2?.clazz

    val instance: Any?
    if (realType != null) {
        instance = DependencyInjector.inject(realType)
    } else {
        val paramDependencyKey = DependencyKey.createDependencyKey(parameter)
        val paramType = parameter.type
        val subType = converter.convertType(paramDependencyKey, paramType)
        instance = DependencyInjector.inject(subType.jvmErasure)

        DependencyInjector.caching(paramDependencyKey, instance)
    }
    instance
}.toTypedArray()
