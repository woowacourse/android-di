package woowacourse.shopping.util.autoDI.lifecycleTypeRegister

import woowacourse.shopping.util.autoDI.dependencyContainer.LifeCycleTypes

class DisposableRegister(private val disposables: LifeCycleTypes.Disposables) {
    fun <T : Any> register(qualifier: String? = null, initializeMethod: () -> T) {
        disposables.add(qualifier, initializeMethod)
    }
}
