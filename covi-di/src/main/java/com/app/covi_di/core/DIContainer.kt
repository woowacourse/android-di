package com.app.covi_di.core

import com.app.covi_di.annotation.Qualifier
import com.app.covi_di.module.DependencyModule
import com.app.covi_di.module.Provider
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

object DIContainer {
    private val moduleInstances = mutableMapOf<KClass<*>, MutableList<KClass<*>>>()
    private val providerInstances = mutableMapOf<KClass<*>, Any>()

    private const val ERROR_PROVIDER_NOT_CONTAINED = "Provider is not contained in DIContainer"
    private const val ERROR_QUALIFIER_MUST_BE_ONE = "Qualifier must be one in implemenation"

    fun init(moduleList: List<DependencyModule>, providerList: List<Provider>) {
        moduleList.map {
            it.invoke().map { (key, value) ->
                moduleInstances[key] =
                    moduleInstances[key]?.apply { add(value) } ?: mutableListOf(value)
            }
        }
        providerList.map {
            providerInstances.putAll(it.get())
        }
    }

    fun getModuleKClass(clazz: KClass<*>): KClass<*>? {
        val result = moduleInstances[clazz] ?: return null

        if (result.size > 1) {
            result.find {
                it.findAnnotation<Qualifier>()?.type == clazz.findAnnotation<Qualifier>()?.type
            } ?: throw IllegalStateException(ERROR_QUALIFIER_MUST_BE_ONE)
        }
        return result.firstOrNull()
    }

    fun getProviderInstance(clazz: KClass<*>): Any {
        return providerInstances[clazz] ?: throw IllegalArgumentException(
            ERROR_PROVIDER_NOT_CONTAINED
        )
    }
}