package io.hyemdooly.di

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.isSubclassOf

object Container {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun addInstance(instance: Any) {
        instances[instance::class] = instance
    }

    fun addInstances(module: Module) {
        module::class.declaredMemberFunctions.forEach { provider ->
            provider.call(module)?.let {
                addInstance(it)
            }
        }
    }

    fun getInstance(type: KClass<*>): Any? {
        println(type)
        if (instances[type] != null) return instances[type]
        val key = instances.keys.firstOrNull { it.isSubclassOf(type) }
        println(key)
        return instances[key]
    }

    fun clear() {
        instances.clear()
    }
}
