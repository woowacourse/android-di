package com.woowacourse.bunadi.util

import com.woowacourse.bunadi.annotation.Inject
import com.woowacourse.bunadi.annotation.Qualifier
import com.woowacourse.bunadi.injector.DependencyKey
import com.woowacourse.bunadi.injector.Injector
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

fun <T : Any> KClass<T>.validateHasPrimaryConstructor(): KFunction<T> {
    return requireNotNull(primaryConstructor) {
        "[ERROR] ${this.simpleName} : 주생성자가 존재하지 않습니다."
    }
}

fun <T> KFunction<T>.createInstance(injector: Injector): T {
    return call(*parameters.createInstances(injector))
}

fun List<KParameter>.createInstances(injector: Injector): Array<Any> = map { parameter ->
    val realType = parameter.parseFromQualifier()
    val paramDependencyKey = DependencyKey.createDependencyKey(parameter)

    val instance = if (realType != null) {
        injector.inject(realType)
    } else {
        injector.inject(parameter.type.jvmErasure)
    }
    injector.caching(paramDependencyKey, instance)
    instance
}.toTypedArray()

fun KParameter.parseFromQualifier(): KClass<*>? {
    val annotation = annotations.firstOrNull()
    return annotation?.parseClassInQualifier()
}

fun KProperty<*>.parseFromQualifier(): KClass<*>? {
    val annotation = annotations.find { it !is Inject }
    return annotation?.parseClassInQualifier()
}

private fun Annotation.parseClassInQualifier(): KClass<*>? {
    val annotation = annotationClass
    val qualifier = annotation.annotations.find { it is Qualifier } as? Qualifier
    return qualifier?.clazz
}
