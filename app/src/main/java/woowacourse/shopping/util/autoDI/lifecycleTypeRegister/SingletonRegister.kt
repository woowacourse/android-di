package woowacourse.shopping.util.autoDI.lifecycleTypeRegister

import woowacourse.shopping.util.autoDI.LifeCycleType

class SingletonRegister(private val singletons: MutableList<LifeCycleType.Singleton<*>>) {
    fun <T : Any> register(qualifier: String? = null, initializeMethod: () -> T) {
        singletons.add(LifeCycleType.Singleton(qualifier, initializeMethod))
    }
}
