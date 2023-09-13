package io.hyemdooly.di

import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

object Container {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun addInstance(instance: Any) {
        println(instance::class)
        instances[instance::class] = instance
    }

    fun getInstance(type: KClass<*>): Any? {
        if (instances[type] != null) instances[type]
        val key = instances.keys.firstOrNull { it.isSubclassOf(type) }
        return instances[key]
    }

    fun clear() {
        instances.clear()
    }
}
