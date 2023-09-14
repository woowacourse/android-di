package com.re4rk.arkdi.annotations

import com.re4rk.arkdi.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER

@Retention(RUNTIME)
@Target(VALUE_PARAMETER, FUNCTION)
@Qualifier
annotation class StorageType(val type: Type) {
    enum class Type { DATABASE, IN_MEMORY }
}
