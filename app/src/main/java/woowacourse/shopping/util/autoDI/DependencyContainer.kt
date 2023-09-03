package woowacourse.shopping.util.autoDI

import woowacourse.shopping.util.autoDI.lifecycleTypeRegister.SingletonRegister

object DependencyContainer {
    private val singletons: MutableList<LifeCycleType.Singleton<*>> = mutableListOf()
//    private val disposables: MutableList<LifeCycleType.Disposable<*>> = mutableListOf()
//    private val activities: MutableList<LifeCycleType.Activity<*>> = mutableListOf()
//    private val fragments: MutableList<LifeCycleType.Fragment<*>> = mutableListOf()

    val singletonRegister: SingletonRegister = SingletonRegister(singletons)
}
