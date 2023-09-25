package com.di.berdi.util

import androidx.lifecycle.ViewModel
import java.lang.reflect.Field
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaField

internal fun <T : KProperty<*>> Collection<T>.findViewModelField(): Field? {
    return firstOrNull {
        val fieldType = requireNotNull(it.javaField?.type)
        ViewModel::class.java.isAssignableFrom(fieldType)
    }?.javaField
}
