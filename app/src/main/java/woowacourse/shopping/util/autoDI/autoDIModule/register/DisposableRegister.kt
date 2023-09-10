package woowacourse.shopping.util.autoDI.autoDIModule.register

import woowacourse.shopping.util.autoDI.autoDIModule.LifeCycleTypes

class DisposableRegister(private val disposables: LifeCycleTypes.Disposables) {
    internal fun <T : Any> register(qualifier: String? = null, initializeMethod: () -> T) {
        disposables.add(qualifier, initializeMethod)
    }
}
