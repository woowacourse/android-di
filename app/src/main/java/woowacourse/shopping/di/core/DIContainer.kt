package woowacourse.shopping.di.core

import woowacourse.shopping.di.module.DependencyModule
import kotlin.reflect.KClass

object DIContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun init(moduleList: List<DependencyModule>) {
        moduleList.map {
            instances.putAll(it.invoke())
        }
    }

    fun get(clazz: KClass<*>): Any {
        return instances[clazz] ?: throw IllegalArgumentException()
    }
}