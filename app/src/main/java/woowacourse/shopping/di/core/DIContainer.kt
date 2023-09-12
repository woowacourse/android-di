package woowacourse.shopping.di.core

import android.content.Context
import woowacourse.shopping.di.module.DependencyModule
import woowacourse.shopping.di.module.Provider
import kotlin.reflect.KClass

object DIContainer {
    private val moduleInstances = mutableMapOf<KClass<*>, KClass<*>>()
    private val providerInstances = mutableMapOf<KClass<*>, Any>()

    fun init(moduleList: List<DependencyModule>, providerList: List<Provider>, context: Context) {
        moduleList.map {
            moduleInstances.putAll(it.invoke(context))
        }
        providerList.map {
            it.init(context)
            providerInstances.putAll(it.get())
        }
    }

    fun getModuleKClass(clazz: KClass<*>): KClass<*>? {
        return moduleInstances[clazz]
    }

    fun getProviderInstance(clazz: KClass<*>): Any {
        return providerInstances[clazz] ?: throw IllegalArgumentException()
    }
}