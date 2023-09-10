package woowacourse.shopping.common

import kotlin.reflect.KClass

class DefaultAppContainer private constructor() : AppContainer {

    private val instances: HashMap<KClass<*>, Any> = HashMap()

    override fun getInstance(type: KClass<*>): Any? {
        return instances[type]
    }

    override fun addInstance(type: KClass<*>, instance: Any) {
        instances[type] = instance
    }

    override fun clear() {
        instances.clear()
    }

    companion object {
        private var DEFAULT_APP_CONTAINER: DefaultAppContainer? = null

        operator fun invoke(): DefaultAppContainer {
            if (DEFAULT_APP_CONTAINER == null) DEFAULT_APP_CONTAINER = DefaultAppContainer()
            return DEFAULT_APP_CONTAINER!!
        }
    }
}
