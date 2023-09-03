package woowacourse.shopping.di

import kotlin.reflect.KClass

object DIContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun init() {
        RepositoryModule.init()
    }

    fun bind(clazz: KClass<*>, instance: Any) {
        instances[clazz] = instance
    }

    fun get(clazz: KClass<*>): Any {
        return instances[clazz] ?: throw IllegalArgumentException()
    }
}