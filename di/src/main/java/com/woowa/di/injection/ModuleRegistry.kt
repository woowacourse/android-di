package com.woowa.di.injection

import kotlin.reflect.KClass

object ModuleRegistry {
    private val moduleMap =
        mutableMapOf<KClass<*>, KClass<out Module<*, *>>>()

    fun getModuleForType(key: KClass<out Any>?): KClass<out Module<*, *>>? {
        return moduleMap[key]
    }

    fun <T : Any, type : Any> registerModule(
        key: KClass<out type>,
        module: KClass<out Module<T, type>>,
    ) {
        moduleMap[key] = module
    }

    fun moduleTypes(): Set<KClass<*>> {
        return moduleMap.keys
    }
}
