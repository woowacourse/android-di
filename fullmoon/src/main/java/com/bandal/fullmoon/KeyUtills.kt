package com.bandal.fullmoon

import kotlin.reflect.KCallable
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation

internal fun KParameter.getKey(): String {
    val type = this.type
    val qualifier: String? = this.findAnnotation<Qualifier>()?.key
    return "$type${qualifier ?: ""}"
}

internal fun KProperty1<*, *>.getKey(): String {
    val type = this.returnType
    val qualifier: String? = this.findAnnotation<Qualifier>()?.key
    return "$type${qualifier ?: ""}"
}

internal fun KCallable<*>.getKey(): String {
    val type: KType = this.returnType
    val qualifier: String? = this.findAnnotation<Qualifier>()?.key
    return "$type${qualifier ?: ""}"
}
