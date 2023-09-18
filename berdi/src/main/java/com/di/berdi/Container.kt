package com.di.berdi

import kotlin.reflect.KClass

class Container(private val store: HashMap<StoreKey, Any> = hashMapOf()) {

    fun getInstance(type: KClass<*>, qualifiedName: String?): Any? {
        return store[StoreKey(type, qualifiedName)]
    }

    fun setInstance(instance: Any, type: KClass<*>, qualifiedName: String?) {
        store[StoreKey(type, qualifiedName)] = instance
    }

    data class StoreKey(
        val clazz: KClass<*>,
        val qualifiedName: String?,
    )
}
