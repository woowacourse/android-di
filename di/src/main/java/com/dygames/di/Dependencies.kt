package com.dygames.di

import kotlin.reflect.KType

data class Dependencies(val qualifiers: HashMap<Annotation?, Qualifier> = hashMapOf()) {
    fun findDependency(type: KType, qualifier: Annotation?): Any? {
        return qualifiers[qualifier]?.let {
            it.constructors[type]?.let { constructor ->
                DependencyInjector.instantiate(constructor)
            } ?: it.providers[type]?.let { provider ->
                provider()
            }
        }
    }
}
