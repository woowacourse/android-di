package com.di.berdi.util

import androidx.lifecycle.ViewModel
import java.lang.reflect.Field
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField

val KClass<*>.declaredViewModelFields
    get() = this.declaredMemberProperties
        .asSequence()
        .filter {
            val fieldType = requireNotNull(it.javaField?.type)
            ViewModel::class.java.isAssignableFrom(fieldType)
        }.map {
            requireNotNull(it.javaField)
        }

internal fun <T> Field.setInstance(target: Any, instance: T) {
    isAccessible = true
    set(target, instance)
}
