package com.buna.di.injector

import kotlin.reflect.KType

class SubTypeConverter(
    private val converter: MutableMap<DependencyKey, KType> = mutableMapOf(),
) {
    fun saveType(parentTypeKey: DependencyKey, childType: KType) {
        converter[parentTypeKey] = childType
    }

    fun convertType(parentTypeKey: DependencyKey, defaultType: KType): KType {
        return converter[parentTypeKey] ?: defaultType
    }
}
