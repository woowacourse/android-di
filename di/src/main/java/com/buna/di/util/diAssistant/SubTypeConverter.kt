package com.buna.di.util.diAssistant

import com.buna.di.injector.DependencyKey
import kotlin.reflect.KType

data class SubTypeConverter(
    private val converter: MutableMap<DependencyKey, KType> = mutableMapOf(),
) {
    fun saveType(parentTypeKey: DependencyKey, childType: KType) {
        converter[parentTypeKey] = childType
    }

    fun convertType(parentTypeKey: DependencyKey, defaultType: KType): KType {
        return converter[parentTypeKey] ?: defaultType
    }

    fun clear(): SubTypeConverter {
        return copy(converter = mutableMapOf())
    }
}
