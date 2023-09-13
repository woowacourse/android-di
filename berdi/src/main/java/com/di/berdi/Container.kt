package com.di.berdi

import kotlin.reflect.KClass

class Container {
    private val store: HashMap<StoreKey, Any> = hashMapOf()

    fun getInstance(type: KClass<*>, annotation: Annotation?): Any? {
        return store[StoreKey(type, annotation)]
    }

    fun setInstance(instance: Any, type: KClass<*>, annotation: Annotation?) {
        store[StoreKey(type, annotation)] = instance
    }

    data class StoreKey(
        val clazz: KClass<*>,
        val qualifier: Annotation?,
    )
}
