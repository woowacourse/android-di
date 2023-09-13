package woowacourse.shopping.di.core

import android.content.Context
import woowacourse.shopping.di.annotation.Qualifier
import woowacourse.shopping.di.module.DependencyModule
import woowacourse.shopping.di.module.Provider
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

object DIContainer {
    private val moduleInstances = mutableMapOf<KClass<*>, MutableList<KClass<*>>>()
    private val providerInstances = mutableMapOf<KClass<*>, Any>()

    fun init(moduleList: List<DependencyModule>, providerList: List<Provider>, context: Context) {
        moduleList.map {
            it.invoke(context).map { (key, value) ->
                moduleInstances[key] = moduleInstances[key]?.apply { add(value) } ?: mutableListOf(value)
            }
        }
        providerList.map {
            it.init(context)
            providerInstances.putAll(it.get())
        }
    }

    fun getModuleKClass(clazz: KClass<*>): KClass<*>? {
        return moduleInstances[clazz]?.find {
             it.hasAnnotation<Qualifier>()
        }

    }

    fun getProviderInstance(clazz: KClass<*>): Any {
        return providerInstances[clazz] ?: throw IllegalArgumentException()
    }
}