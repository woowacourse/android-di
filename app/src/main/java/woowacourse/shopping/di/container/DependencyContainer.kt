package woowacourse.shopping.di.container

import kotlin.reflect.KClass

object DependencyContainer {
    private val instances: HashMap<KClass<*>, Any> = hashMapOf()
    fun setInstance(kClass: KClass<*>, instance: Any) {
        instances[kClass] = instance
    }

    fun getInstance(kClass: KClass<*>): Any? {
        return instances[kClass]
    }
}
