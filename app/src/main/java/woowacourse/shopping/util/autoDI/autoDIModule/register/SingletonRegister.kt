package woowacourse.shopping.util.autoDI.autoDIModule.register

import woowacourse.shopping.util.autoDI.autoDIModule.LifeCycleTypes

class SingletonRegister(private val singletons: LifeCycleTypes.Singletons) {
    internal fun <T : Any> register(qualifier: String? = null, initializeMethod: () -> T) {
        singletons.add(qualifier, initializeMethod)
    }
}
