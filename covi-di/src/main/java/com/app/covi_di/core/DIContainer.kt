package com.app.covi_di.core

import android.content.Context
import com.app.covi_di.annotation.Qualifier
import com.app.covi_di.module.DependencyModule
import com.app.covi_di.module.Provider
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

object DIContainer {
    private val moduleInstances = mutableMapOf<KClass<*>, MutableList<KClass<*>>>()
    private val providerInstances = mutableMapOf<KClass<*>, Any>()

    private const val ERROR_PROVIDER_NOT_CONTAINED = "Provider is not contained in DIContainer"
    private const val ERROR_QUALIFIER_MUST_BE_ONE = "Qualifier must be one in implemenation"

    fun init(moduleList: List<DependencyModule>, providerList: List<Provider>, context: Context) {
        moduleList.map {
            it.invoke().map { (key, value) ->
                moduleInstances[key] =
                    moduleInstances[key]?.apply { add(value) } ?: mutableListOf(value)
            }
        }
        providerList.map {
            it.init(context)
            providerInstances.putAll(it.get())
        }
    }

    fun getModuleKClass(clazz: KClass<*>): KClass<*>? {
        val result = moduleInstances[clazz]?.filter {
            it.hasAnnotation<Qualifier>()
        } ?: return null
        if (result.size > 1) throw IllegalArgumentException(ERROR_QUALIFIER_MUST_BE_ONE)
        return result.first()
    }

    fun getProviderInstance(clazz: KClass<*>): Any {
        return providerInstances[clazz] ?: throw IllegalArgumentException(
            ERROR_PROVIDER_NOT_CONTAINED
        )
    }
}