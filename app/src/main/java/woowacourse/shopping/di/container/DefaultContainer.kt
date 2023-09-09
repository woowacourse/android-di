package woowacourse.shopping.di.container

import kotlin.reflect.KClass

class DefaultContainer : ShoppingContainer {
    private val instances = mutableMapOf<KClass<*>, Any?>()

    override fun <T : Any> createInstance(clazz: KClass<T>, instance: T) {
        instances[clazz] = instance
    }

    override fun <T : Any> getInstance(clazz: KClass<T>): T? {
        return instances[clazz] as? T
    }

    override fun clearInstance() {
        instances.clear()
    }
}

