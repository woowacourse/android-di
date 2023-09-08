package woowacourse.shopping.di

import kotlin.reflect.KClass

class Container {
    private val store: HashMap<KClass<*>, Any> = hashMapOf()

    fun getInstance(type: KClass<*>): Any? {
        return store[type]
    }

    fun setInstance(type: KClass<*>, instance: Any) {
        store[type] = instance
    }
}
