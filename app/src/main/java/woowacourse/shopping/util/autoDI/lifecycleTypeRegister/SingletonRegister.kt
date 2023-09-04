package woowacourse.shopping.util.autoDI.lifecycleTypeRegister

import woowacourse.shopping.util.autoDI.dependencyContainer.LifeCycleTypes

class SingletonRegister(private val singletons: LifeCycleTypes.Singletons) {
    fun <T : Any> register(qualifier: String? = null, initializeMethod: () -> T) {
        singletons.add(qualifier, initializeMethod)
    }
}
