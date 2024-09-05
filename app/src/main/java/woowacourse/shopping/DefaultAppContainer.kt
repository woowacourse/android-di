package woowacourse.shopping

import woowacourse.di.InjectedContainer

class DefaultAppContainer : AppContainer() {
    override val injectedComponentContainer: InjectedContainer by lazy {
        InjectedContainer()
    }
}
