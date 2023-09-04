package woowacourse.shopping.util.autoDI.lifecycleTypeRegister

import woowacourse.shopping.util.autoDI.LifeCycleType

class DisposableRegister(private val disposables: MutableList<LifeCycleType.Disposable<*>>) {
    fun <T : Any> register(qualifier: String? = null, initializeMethod: () -> T) {
        disposables.add(LifeCycleType.Disposable(qualifier, initializeMethod))
    }
}
