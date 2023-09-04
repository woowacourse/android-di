package woowacourse.shopping.util.autoDI

import woowacourse.shopping.util.autoDI.lifecycleTypeRegister.DisposableRegister
import woowacourse.shopping.util.autoDI.lifecycleTypeRegister.SingletonRegister

// 추후 모듈 분리로 접근 제한자 internal 로 설정
internal object DependencyContainer {
    internal val singletons: MutableList<LifeCycleType.Singleton<*>> = mutableListOf()
    internal val disposables: MutableList<LifeCycleType.Disposable<*>> = mutableListOf()
//    private val activities: MutableList<LifeCycleType.Activity<*>> = mutableListOf()
//    private val fragments: MutableList<LifeCycleType.Fragment<*>> = mutableListOf()

    val singletonRegister: SingletonRegister = SingletonRegister(singletons)
    val disposableRegister: DisposableRegister = DisposableRegister(disposables)
}
